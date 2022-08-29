//
//  TweetTableViewCell.swift
//  SmashTag Mentions
//
//  Created by J L on 7/21/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

// TODO: This application is modifying the autolayout engine from a background thread, which can lead to engine corruption and weird crashes.  This will cause an exception in a future release. 
// something ot do with grand central d

import UIKit
import Twitter

class TweetTableViewCell: UITableViewCell {
    
    @IBOutlet weak var tweetCreatedLabel: UILabel!
    @IBOutlet weak var tweetProfileImageView: UIImageView!
    @IBOutlet weak var tweetTextLabel: UILabel!
    @IBOutlet weak var tweetScreenNameLabel: UILabel!
    
    var tweet: Twitter.Tweet? {
        didSet {
            updateUI()
        }
    }
    
    fileprivate func highlightAttributedStringWithColor(_ color: UIColor, attributedString: NSMutableAttributedString, mentionsArrayToHighlight: [Twitter.Mention]) {
        for mention in mentionsArrayToHighlight {
            attributedString.setAttributes([NSForegroundColorAttributeName : color], range: mention.nsrange)
        }
    }
    
    fileprivate func updateUI() {
        // reset any existing tweet information
        tweetTextLabel?.attributedText = nil
        tweetScreenNameLabel?.text = nil
        tweetProfileImageView?.image = nil
        tweetCreatedLabel?.text = nil
        
        // load new information from our tweet (if any)
        if let tweet = self.tweet { // non nil tweet
            // get the text and add all the camera emotes first
            var tweetText = tweet.text
            for _ in tweet.media {
                tweetText += " 📷"
            }
            tweetTextLabel?.text = tweetText // set the text to the outlet
            var highlightedText: NSMutableAttributedString {
                let attributedString = NSMutableAttributedString(string: (tweetTextLabel?.text)!)
                highlightAttributedStringWithColor(UIColor.blueColor(), attributedString: attributedString, mentionsArrayToHighlight: tweet.hashtags)
                highlightAttributedStringWithColor(UIColor.orangeColor(), attributedString: attributedString, mentionsArrayToHighlight: tweet.urls)
                highlightAttributedStringWithColor(UIColor.lightGrayColor(), attributedString: attributedString, mentionsArrayToHighlight: tweet.userMentions)
                return attributedString
            }
            tweetTextLabel?.attributedText = highlightedText // get the .text from outlet and override with highlighted attributed text
        
            tweetScreenNameLabel.text = "\(tweet.user)" // tweet.user.description
            
            
            if let profileImageURL = tweet.user.profileImageURL {
                DispatchQueue.global(qos: DispatchQoS.QoSClass.userInitiated).async(execute: {
                    [weak weakSelf = self] in
                    if let imageData = Data(contentsOfURL: profileImageURL) {
                        DispatchQueue.main.async(execute: {
                            weakSelf?.tweetProfileImageView?.image = UIImage(data: imageData)
                        })
                    }
                })
            }
            
            let formatter = DateFormatter()
            if Date().timeIntervalSinceDate(tweet.created) > 24*60*60 {
                formatter.dateStyle = DateFormatter.Style.short
            } else {
                formatter.timeStyle = DateFormatter.Style.short
            }
            tweetCreatedLabel?.text = formatter.stringFromDate(tweet.created)
        }
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }

}
