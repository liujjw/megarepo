//
//  UserHashtagMention.swift
//  SmashTag Mentions
//
//  Created by J L on 8/1/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

// TODO: double count mentions!, ID field??
// 1. Must be specific to a certain search term, else different search terms
//    that come up with the same tweet will not get counted
// 2. maybe like an id object, but we won't actually need id data that much and storing might be wasteful
// 3. should be encapsulated in this class, cuz we want the db to have this feature regardless of any outside inplmentation

import Foundation
import CoreData
import Twitter

class UserHashtagMention: NSManagedObject {

    static func insertTweetMentionsIntoDatabase(_ context: NSManagedObjectContext, forTweet newTweet: Twitter.Tweet, fromSearchTerm searchText: String) {
        // when the same term is searched, dont double count its mentions, only new tweets should be used
        if UsedTweet.tweetHasBeenAssociatedAlreadyForSearchTermElseInsert(newTweet, forContext: context, forSearchTerm: searchText) {
            return
        }
        // add searchtext into db, 
        // or do nothing if already there
        // then look at its related mentions and see if any new user/hashtag mentions consolidated from the given tweet match it
            // if yes, then update the count of that mention and remove that mention from the big array
            // if no, then create a new mention from the remaining elements in the big array
        let tweetTextToPullMentionsFrom = newTweet.text as NSString
        var consolidatedMentionsFromTweet = Array<Twitter.Mention>()
        consolidatedMentionsFromTweet.appendContentsOf(newTweet.userMentions)
        consolidatedMentionsFromTweet.appendContentsOf(newTweet.hashtags)
        
        if let mentionSetFromSearchTerm = SearchTerm.insertOrGetSearchTermFromDatabase(context, forSearchTerm: searchText)?.mentions {
            for element in mentionSetFromSearchTerm {
                if let relatedMentionToSearchTerm = element as? UserHashtagMention {
                    for mention in consolidatedMentionsFromTweet {
                        // http://stackoverflow.com/questions/27040924/nsrange-from-swift-range, using nsattributed string has 
                        // {dictionary attribute mapping code} for each mention, so use nsstring instead, or make use of Swift Range by converting NSRange to Range
                        if String(tweetTextToPullMentionsFrom.substringWithRange(mention.nsrange)).caseInsensitiveCompare(relatedMentionToSearchTerm.text!) == .OrderedSame {
                            if let numberOfTimesMentioned = relatedMentionToSearchTerm.numberOfTimesMentioned as? Int {
                                relatedMentionToSearchTerm.numberOfTimesMentioned = NSNumber(integer: numberOfTimesMentioned + 1)
                                // remove from array so mention doesn't increase its numberoftiems mentioned AND get added again to the db
                                consolidatedMentionsFromTweet.removeAtIndex(consolidatedMentionsFromTweet.indexOf(mention)!)
                            }
                        }
                    }
                }
            }
        }
        for remainingMention in consolidatedMentionsFromTweet {
            // if in a given tweet, there are duplicate mentions, look into db
            // if one of the mentions duplicated was already inserted, don't insert again
            // what if we get here from an entirely different tweet, wouldnt this interfere with the 
            // number of times mentioned increment?, shoudl NOT bc the above code would have removed
            // it from the array
            let request = NSFetchRequest(entityName: "UserHashtagMention")
            request.predicate = NSPredicate(format: "text = %@", String(tweetTextToPullMentionsFrom.substringWithRange(remainingMention.nsrange)))
            if context.countForFetchRequest(request, error: nil) >= 1 {
                continue
            }
            
            if let newMentionObject = NSEntityDescription.insertNewObjectForEntityForName("UserHashtagMention", inManagedObjectContext: context) as? UserHashtagMention {
                newMentionObject.text = String(tweetTextToPullMentionsFrom.substringWithRange(remainingMention.nsrange))
                newMentionObject.numberOfTimesMentioned = 1
                newMentionObject.fromSearchTerm = SearchTerm.insertOrGetSearchTermFromDatabase(context, forSearchTerm: searchText)
                // Setting the mention's relationship to searchterm also sets searchterm's relationship set to it automatically.
            }
        }
        
    }
    
    
    
}
