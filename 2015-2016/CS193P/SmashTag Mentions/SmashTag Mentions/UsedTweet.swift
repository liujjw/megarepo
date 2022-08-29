//
//  UsedTweet.swift
//  SmashTag Mentions
//
//  Created by J L on 8/4/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import Foundation
import CoreData
import Twitter

class UsedTweet: NSManagedObject {
    
    static func tweetHasBeenAssociatedAlreadyForSearchTermElseInsert(_ tweet: Twitter.Tweet, forContext context: NSManagedObjectContext, forSearchTerm searchText: String) -> Bool {
        // takes search term because it will allow same tweets from different search terms but not same tweets from same search term to be put into database
        let request = NSFetchRequest(entityName: "UsedTweet")
        request.predicate = NSPredicate(format: "tweetID = %@ and fromSearchTerm.text = %@", tweet.id, searchText)
        if context.count(for: request, error: nil) >= 1 {
            return true
        } else {
            if let newUsedTweet = NSEntityDescription.insertNewObject(forEntityName: "UsedTweet", into: context) as? UsedTweet {
                newUsedTweet.tweetID = tweet.id
                newUsedTweet.fromSearchTerm = SearchTerm.insertOrGetSearchTermFromDatabase(context, forSearchTerm: searchText)!
            }
            return false
        }
    }
}
