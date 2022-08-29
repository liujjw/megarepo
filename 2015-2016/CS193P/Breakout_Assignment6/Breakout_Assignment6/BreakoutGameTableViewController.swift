//
//  BreakoutGameTableViewController.swift
//  Breakout_Assignment6
//
//  Created by J L on 8/11/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import UIKit

class BreakoutGameTableViewController: UITableViewController {

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 4
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath)

        // Configure the cell...

        return cell
    }
*/
}
