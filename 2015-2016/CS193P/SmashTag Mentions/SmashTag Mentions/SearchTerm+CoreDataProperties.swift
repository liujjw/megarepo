//
//  SearchTerm+CoreDataProperties.swift
//  SmashTag Mentions
//
//  Created by J L on 8/1/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//
//  Choose "Create NSManagedObject Subclass…" from the Core Data editor menu
//  to delete and recreate this implementation file for your updated model.
//

import Foundation
import CoreData

extension SearchTerm {

    @NSManaged var text: String?
    @NSManaged var mentions: NSSet?

}
