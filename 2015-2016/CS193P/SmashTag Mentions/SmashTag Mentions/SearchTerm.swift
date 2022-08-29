//
//  SearchTerm.swift
//  SmashTag Mentions
//
//  Created by J L on 8/1/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import Foundation
import CoreData


class SearchTerm: NSManagedObject {

    static func insertOrGetSearchTermFromDatabase(_ context: NSManagedObjectContext, forSearchTerm searchTerm: String) -> SearchTerm? {
        let request = NSFetchRequest(entityName: "SearchTerm")
        request.predicate = NSPredicate(format: "text = %@", searchTerm) // look for provided search term
        if let result = (try? context.fetch(request))?.first as? SearchTerm {
            return result // dont actually insert if somethings already there 
        } else if let newSearchTermObject = NSEntityDescription.insertNewObject(forEntityName: "SearchTerm", into: context) as? SearchTerm { // add in serach term object if it doesnt exist in the db
            newSearchTermObject.text = searchTerm
            return newSearchTermObject
        }
        return nil
    }
}
