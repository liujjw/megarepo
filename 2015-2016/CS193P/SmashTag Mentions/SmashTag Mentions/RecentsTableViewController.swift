//
//  RecentsTableViewController.swift
//  SmashTag Mentions
//
//  Created by J L on 7/26/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import UIKit
import Twitter
import CoreData

class RecentsTableViewController: UITableViewController, UISplitViewControllerDelegate {
    // NOTE: code that actaully handles adding to nsuserdefaults is in SmashtagTVC
    // can be retrieved and set from any other class, this class merely interprets it for our cells
    let defaults = UserDefaults.standard
    fileprivate let nameOfRecentSearchesInDefaults = "RecentSearches"
    
    // MARK: Model
    // for getting and setting searches from user defaults
    fileprivate var recentSearches: [String]? {
        if let searches = defaults.object(forKey: nameOfRecentSearchesInDefaults) as? [String] {
            return searches
        } else {
            return nil
        }
    }
    
    // MARK: - Table view data source
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let numberOfRecents = recentSearches?.count {
            return numberOfRecents
        }
        return 0
    }
    
    fileprivate struct Storyboard {
        fileprivate static let nameOfRecentSearchTermCell = "Recent Search Term"
    }
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: Storyboard.nameOfRecentSearchTermCell, for: indexPath)
        
        if let recentSearch = recentSearches?[(indexPath as NSIndexPath).row] {
            cell.textLabel?.text = recentSearch
        }
        
        return cell
    }
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "Search This Recent Term" {
            if let searchTVC = segue.destination.contentViewController as? SmashTagTableViewController {
                if let recentsCell = sender as? UITableViewCell {
                    searchTVC.searchText = recentsCell.textLabel?.text
                }
            }
        } else if segue.identifier == "Show Popular Mentions" {
            if let mentionsTVC = segue.destination.contentViewController as? MentionsPopularityTableViewController {
                if let recentsCell = sender as? UITableViewCell {
                    mentionsTVC.searchTerm = recentsCell.textLabel?.text
                    let managedObjectContext: NSManagedObjectContext? = (UIApplication.shared.delegate as? AppDelegate)?.managedObjectContext
                    mentionsTVC.managedObjectContext = managedObjectContext
                    mentionsTVC.title = "Popular Mentions"
                }
            }
        }
    }
    
    // MARK: VCL
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Recent Searches"
        splitViewController?.delegate = self
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false
        
        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }
    // update everytime we come back on screen cuz that means the search tab was probably searching something
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        tableView.reloadData()
    }
    
    // MARK: SplitView delegate
    func splitViewController(_ splitViewController: UISplitViewController, collapseSecondary secondaryViewController: UIViewController, onto primaryViewController: UIViewController) -> Bool {
        if let smashtagTVC = secondaryViewController.contentViewController as? SmashTagTableViewController {
            if smashtagTVC.searchText == nil {
                return true
            }
        }
        return false
    }
    
    /*
     // Override to support conditional editing of the table view.
     override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
     // Return false if you do not want the specified item to be editable.
     return true
     }
     */
    
    /*
     // Override to support editing the table view.
     override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
     if editingStyle == .Delete {
     // Delete the row from the data source
     tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
     } else if editingStyle == .Insert {
     // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
     }
     }
     */
    
    /*
     // Override to support rearranging the table view.
     override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {
     
     }
     */
    
    /*
     // Override to support conditional rearranging of the table view.
     override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
     // Return false if you do not want the item to be re-orderable.
     return true
     }
     */
    
}
