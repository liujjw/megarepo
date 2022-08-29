//
//  UsedTweet+CoreDataProperties.swift
//  SmashTag Mentions
//
//  Created by J L on 8/4/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//
//  Choose "Create NSManagedObject Subclass…" from the Core Data editor menu
//  to delete and recreate this implementation file for your updated model.
//

import Foundation
import CoreData

extension UsedTweet {

    @NSManaged var tweetID: String?
    @NSManaged var fromSearchTerm: SearchTerm?

}
