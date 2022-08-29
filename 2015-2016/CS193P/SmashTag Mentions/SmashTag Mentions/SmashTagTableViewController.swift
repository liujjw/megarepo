//
//  SmashTagTableViewController.swift
//  SmashTag Mentions
//
//  Created by J L on 7/21/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import UIKit
import Twitter // Attatched "Twitter" framework by CS193P Instructor
import CoreData

class SmashTagTableViewController: UITableViewController, UITextFieldDelegate, UISplitViewControllerDelegate {
    
    // MARK: Model 
    var managedObjectContext: NSManagedObjectContext? = (UIApplication.shared.delegate as? AppDelegate)?.managedObjectContext
    // called when a tweet search is performed and gets all new 100 tweets pertinent data into db
    fileprivate func updateDatabaseForTweets(_ tweets: [Twitter.Tweet], forSearchTerm searchTerm: String) {
        managedObjectContext?.perform({ [weak weakSelf = self] in // always perform block
            // searchTerm: text, relationships 
            // userHashtagMention: text, numberOfPointers, relationships
            for tweet in tweets {
                UserHashtagMention.insertTweetMentionsIntoDatabase((weakSelf?.managedObjectContext)!, forTweet: tweet, fromSearchTerm: searchTerm) // moc must not be nil when this gets called, else since force unwrap we'll know
            }
            do {
                try weakSelf?.managedObjectContext?.save() // must manually save when using appdelegate moc
            } catch let error {
                print("Core data saving error: \(error)")
            }
        })
    }
    // main model for the controller and mvc
    fileprivate var arraysOfTweets = [Array<Twitter.Tweet>]() {
        didSet {
            // when set, reset our cells
            tableView.reloadData()
        }
    }
    // allowed to be set and we'll look for it and populate the model
    var searchText: String? {
        didSet {
            arraysOfTweets.removeAll()
            lastTwitterRequest = nil
            searchAndPopulateTweets()
            title = searchText
            RecentsTableViewController.addNewSearchToRecents(searchText!, NSUserDefaultsObject: defaults, nameOfRecentSearchesInDefaults: nameOfRecentSearchesInDefaults)
            
        }
    }
    // for storing recent search terms
    fileprivate let defaults = UserDefaults.standard
    fileprivate let nameOfRecentSearchesInDefaults = "RecentSearches"
    
    // MARK: Fetching tweets
    fileprivate var twitterRequest: Twitter.Request? {
        // only if completely new search text was set
        if lastTwitterRequest == nil {
            if let query = searchText , !query.isEmpty {
                return Twitter.Request(search: query + " -filter:retweets", count: 100)
            }
        }
        // else give back new tweets with same result, eg we accessed this in a reload
        return lastTwitterRequest?.requestForNewer
    }
    
    fileprivate var lastTwitterRequest: Twitter.Request?
    
    fileprivate func searchAndPopulateTweets() {
        if let request = twitterRequest {
            lastTwitterRequest = request
            request.fetchTweets { [weak weakSelf = self] newTweets in
                DispatchQueue.main.async {
                    if request == weakSelf?.lastTwitterRequest {
                        if !newTweets.isEmpty {
                            weakSelf?.arraysOfTweets.insert(newTweets, atIndex: 0)
                            if let search = weakSelf?.searchText {
                                weakSelf?.updateDatabaseForTweets(newTweets, forSearchTerm: search)
                            }
                        }
                    }
                    weakSelf?.refreshControl?.endRefreshing()
                }
            }
        } else {
            self.refreshControl?.endRefreshing()
        }
    }
    // MARK: Refresh
    @IBAction func refresh(_ sender: UIRefreshControl) {
        searchAndPopulateTweets()
    }

    // MARK: - UITableViewDataSource

    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return "\(arraysOfTweets.count - section)"
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return arraysOfTweets.count
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arraysOfTweets[section].count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: StoryBoard.TweetCellIdentifier, for: indexPath)

        let tweet = arraysOfTweets[indexPath.section][indexPath.row]
        if let tweetCell = cell as? TweetTableViewCell {
            tweetCell.tweet = tweet
        }

        return cell
    }
    
    // MARK: Constants
    fileprivate struct StoryBoard {
        static let TweetCellIdentifier = "Tweet"
    }
    
    // MARK: Search bar textfield
    @IBOutlet weak var searchTextField: UITextField! {
        didSet {
            searchTextField.delegate = self
            searchTextField.text = searchText
        }
    }
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        searchText = textField.text
        return true
    }
    
    // MARK: VCL
    override func viewDidLoad() {
        super.viewDidLoad()
        // NSUserDefaults.standardUserDefaults().removeObjectForKey("RecentSearches") // for removing user defaults
        splitViewController?.delegate = self
        tableView.estimatedRowHeight = tableView.rowHeight
        tableView.rowHeight = UITableViewAutomaticDimension
    }
    
    // MARK: - Navigation
    
    // Segue from a cell in the table view using its data transfer to a details view
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "Show Details" {
            if let tweetCellVC = sender as? TweetTableViewCell {
                if let indexPath = tableView.indexPath(for: tweetCellVC) {
                    if let tweetDetailsVC = segue.destination.contentViewController as? TweetMentionsTableViewController {
                        tweetDetailsVC.tweet = arraysOfTweets[indexPath.section][indexPath.row]
                    }
                }
            }
        }
    }
    
    func splitViewController(_ splitViewController: UISplitViewController, collapseSecondary secondaryViewController: UIViewController, onto primaryViewController: UIViewController) -> Bool {
        if primaryViewController.contentViewController == self {
            if let cvc = secondaryViewController.contentViewController as? TweetMentionsTableViewController , cvc.tweet == nil {
                return true
            }
        }
        return false
    }
    
    }

// Gets the view controller embedded in a navigation controller
extension UIViewController {
    var contentViewController: UIViewController {
        if let navcon = self as? UINavigationController {
            return navcon.visibleViewController ?? self
        }
        return self
    }
}
extension RecentsTableViewController {
    // allows setting of defualts to store recent searches
    static func addNewSearchToRecents(_ search: String, NSUserDefaultsObject: UserDefaults, nameOfRecentSearchesInDefaults: String) {
        
        if let searchesAlreadyStored = NSUserDefaultsObject.object(forKey: nameOfRecentSearchesInDefaults) as? [String] {
            // dont allow duplicates and is uniqued case insensitively
            if !searchesAlreadyStored.isEmpty && searchesAlreadyStored.map({ (string) -> String in
                return string.lowercased()
            }).contains(search.lowercased()) {
                return
            }
            
            // take the terms already in there, add the new one, and then add it back to defaults
            var searches = searchesAlreadyStored
            searches.insert(search, at: 0)
            // keep to >= 100 items
            if searches.count > 100 {
                searches.removeLast()
            }
            NSUserDefaultsObject.set(searches, forKey: nameOfRecentSearchesInDefaults)
        } else {
            // if recents havent been set yet, set it and call itself again
            let newArrayToSetRecentSearches = [String]()
            NSUserDefaultsObject.set(newArrayToSetRecentSearches, forKey: nameOfRecentSearchesInDefaults)
            addNewSearchToRecents(search, NSUserDefaultsObject: NSUserDefaultsObject, nameOfRecentSearchesInDefaults: nameOfRecentSearchesInDefaults)
        }
    }
}
