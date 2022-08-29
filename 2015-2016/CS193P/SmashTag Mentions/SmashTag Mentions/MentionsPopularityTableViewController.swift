//
//  MentionsPopularityTableViewController.swift
//  SmashTag Mentions
//
//  Created by J L on 7/31/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import UIKit
import CoreData
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l < r
  case (nil, _?):
    return true
  default:
    return false
  }
}

fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l > r
  default:
    return rhs < lhs
  }
}


class MentionsPopularityTableViewController: CoreDataTableViewController {

    // MARK: Model 
    var searchTerm: String? { didSet { updateUI() } }
    var managedObjectContext: NSManagedObjectContext? { didSet { updateUI() } }
    
    fileprivate func updateUI() {
        if let context = managedObjectContext , searchTerm?.characters.count > 0 {
            let request = NSFetchRequest(entityName: "UserHashtagMention")
            request.predicate = NSPredicate(format: "numberOfTimesMentioned > 1 and fromSearchTerm.text = %@", searchTerm!)
            request.sortDescriptors = [
                NSSortDescriptor(
                    key: "numberOfTimesMentioned",
                    ascending: false
                ),
                NSSortDescriptor(
                key: "text",
                ascending: true,
                selector: #selector(NSString.localizedCaseInsensitiveCompare(_:))
                ) // sort alphabetically after because of when numberoftimesmentioned is equal
            ]
            fetchedResultsController = NSFetchedResultsController(
                fetchRequest: request,
                managedObjectContext: context,
                sectionNameKeyPath: nil,
                cacheName: nil
            )
        } else {
            fetchedResultsController = nil
        }
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Popular Mention Cell", for: indexPath)
        if let mention = fetchedResultsController?.object(at: indexPath) as? UserHashtagMention {
            var mentionText = ""
            var mentionNumberOfTimesMentioned = 0
            mention.managedObjectContext?.performAndWait({
                mentionNumberOfTimesMentioned = (mention.numberOfTimesMentioned?.intValue)!
                mentionText = mention.text!
            })
            cell.textLabel?.text = mentionText
            cell.detailTextLabel?.text = String(mentionNumberOfTimesMentioned) + " mentions"
        }
        

        return cell
    }

    // MARK: VCL
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
