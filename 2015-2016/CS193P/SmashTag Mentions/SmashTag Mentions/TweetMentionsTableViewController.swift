//
//  TweetMentionsTableViewController.swift
//  SmashTag Mentions
//
//  Created by J L on 7/22/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

// NOTE: From assignmnet spec: "Warning: Twitter does not always report every single mention (especially url mentions at the end of a tweet)."
import UIKit
import Twitter

class TweetMentionsTableViewController: UITableViewController {

    // MARK: Model
    var tweet: Twitter.Tweet? { // BEWARE: if this is nil when the table methods are called, will crash
        didSet {
            tableView.reloadData()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Mentions"
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = true // idk what this does

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }


    // MARK: - Table view data source
    // associating sections to type of mentions
    fileprivate struct Storyboard {
        static let imagesCellName = "Images"
        static let hashtagsCellName = "Hashtags"
        static let userMentionsCellName = "User Mentions"
        static let URLsCellName = "URLs"
        static let sectionNumberForImages = 0
    }
    fileprivate func sectionNumberForMentionName(_ sectionNumber: Int) -> String? {
        switch sectionNumber {
        case 0: return Storyboard.imagesCellName
        case 1: return Storyboard.hashtagsCellName
        case 2: return Storyboard.userMentionsCellName
        case 3: return Storyboard.URLsCellName
        default: return nil
        }
    }
    fileprivate func mentionNameForData(_ mentionName: String) -> AnyObject? {
        if let tweet = self.tweet {
            switch mentionName {
            case Storyboard.imagesCellName: return tweet.media
            case Storyboard.hashtagsCellName: return tweet.hashtags
            case Storyboard.userMentionsCellName: return tweet.userMentions
            case Storyboard.URLsCellName: return tweet.urls
            default: return nil
            }
        }
        return nil
    }
    
    
    fileprivate let numberOfSections = 4
    override func numberOfSections(in tableView: UITableView) -> Int {
        return numberOfSections
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // a section's number of rows depends on its corresponding number of items in the tweet of the type it are supposed to show
        if let mentionName = sectionNumberForMentionName(section) {
            if let mentionData = mentionNameForData(mentionName) as? [Twitter.Mention] {
                return mentionData.count
            } else if let mentionDataMedia = mentionNameForData(mentionName) as? [MediaItem] {
                return mentionDataMedia.count
            }
        }
        print("Unknown section number.")
        return 0
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // get cell, cast into its subclass based on the identifer, and then get its data based on its row number
        if (indexPath as NSIndexPath).section > numberOfSections {
            // throw an error
            print("Unknown section number")
        }
        let cell = tableView.dequeueReusableCell(withIdentifier: sectionNumberForMentionName((indexPath as NSIndexPath).section)!, for: indexPath)
        
        // if image, use custom call, else, use regular default cell properties 
        if let imageCell = cell as? ImagesTableViewCell {
            imageCell.imageURL = tweet?.media[indexPath.row].url // async!
        }  else {
            let nsStringToUseWithNSRange = NSAttributedString(string: tweet!.text)
            if let mentionsArrayToPullDataFrom = mentionNameForData(sectionNumberForMentionName(indexPath.section)!) as? [Twitter.Mention] {
                // set attributed string because if you cast to String it adds on {\n} idk y 
                cell.textLabel?.attributedText = nsStringToUseWithNSRange.attributedSubstringFromRange(mentionsArrayToPullDataFrom[indexPath.row].nsrange)
            }
        }
        
        return cell
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        //  if a section has no items in it, there should be no header visible for that section.
        if let identifier = sectionNumberForMentionName(section) {
            if let dataArrays = mentionNameForData(identifier) {
                if let mentionDataArray = dataArrays as? [Mention] {
                    if !mentionDataArray.isEmpty {
                        return identifier
                    }
                } else if let mediaItemDataArray = dataArrays as? [MediaItem] {
                    if !mediaItemDataArray.isEmpty {
                        return identifier
                    }
                }
            }
        }
        return nil
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        // only alt height for images
        if ((indexPath as NSIndexPath).section == Storyboard.sectionNumberForImages) {
            // since image constraints will always stretch the image to fit the width, we can deduce height with aspect ratio
            if let aspectRatio = tweet?.media[indexPath.row].aspectRatio {
                return tableView.bounds.width / CGFloat(aspectRatio)
            }
            
        }
        return tableView.rowHeight
    }
    
    // if url row selected, open in safari
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if sectionNumberForMentionName((indexPath as NSIndexPath).section) == Storyboard.URLsCellName {
            // get the cells text data for url
            if let urlString = tableView.cellForRow(at: indexPath)?.textLabel?.text {
                if let url = URL(string: urlString) {
                    UIApplication.shared.openURL(url)
                }
            }
        }
    }
    
     // MARK: - Navigation
     
     // Segue from a user mention or hastag to search it in a new tweet view
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let identifier = segue.identifier {
            switch identifier {
            case "Search This Hashtag", "Search This User":
                if let searchTVC = segue.destination.contentViewController as? SmashTagTableViewController {
                    if let tvCell = sender as? UITableViewCell {
                        if let textLabel = tvCell.textLabel?.text {
                            searchTVC.searchText = textLabel
                        }
                    }
                }
            case "View Image In More Detail":
                if let scrollVC = segue.destination.contentViewController as? ScrollViewImageViewController {
                    if let imageCell = sender as? ImagesTableViewCell {
                        if let image = imageCell.imageViewOutlet?.image {
                            scrollVC.image = image
                        }
                    }
                }
            default: return
            }
        }
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
