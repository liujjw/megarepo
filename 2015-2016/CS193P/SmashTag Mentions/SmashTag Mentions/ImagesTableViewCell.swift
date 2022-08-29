//
//  ImagesTableViewCell.swift
//  SmashTag Mentions
//
//  Created by J L on 7/22/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import UIKit

class ImagesTableViewCell: UITableViewCell {
    
    // so give this class an image url and aspect ratio 
    // once set, this works on getting the data for the 
    // ui image, which once set, finally set the 
    // outlet
    
    var imageURL: URL? {
        willSet {
            DispatchQueue.global(qos: DispatchQoS.QoSClass.userInitiated).async {
                [weak weakSelf = self] in
                if let imageData = try? Data(contentsOf: newValue!) , newValue != nil {
                    DispatchQueue.main.async(execute: {
                        weakSelf?.imageToBeSet = UIImage(data: imageData)
                    })
                }
            }
        }
    }
    
    fileprivate var imageToBeSet: UIImage? {
        didSet {
            imageViewOutlet.image = imageToBeSet
        }
    }

    @IBOutlet weak var imageViewOutlet: UIImageView!

}

