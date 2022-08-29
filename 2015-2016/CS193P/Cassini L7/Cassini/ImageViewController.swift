//
//  ImageViewController.swift
//  Cassini
//
//  Created by CS193p Instructor.
//  Copyright © 2016 Stanford University. All rights reserved.
//

import UIKit

class ImageViewController: UIViewController
{
    // MARK: Model
    var imageURL: NSURL? {
        didSet {
            // load up image from model
            image = nil
            fetchImage()
        }
    }
    
    private func fetchImage() {
        if let url = imageURL {
            // collect bag o bits
            if let imageData = NSData(contentsOfURL: url) {
                image = UIImage(data: imageData)
            }
        }
    }
    
    // Outlet to scoll view
    @IBOutlet weak var scrollView: UIScrollView! {
        didSet {
            scrollView.contentSize = imageView.frame.size
        }
    }
    
    // Setting uiviews programmatically
    private var imageView = UIImageView()
    
    // Solves framing of imageView and its emptiness.
    private var image: UIImage? {
        get {
            return imageView.image
        }
        set {
            imageView.image = newValue
            // Frame
            imageView.sizeToFit()
            // if segueing outlets wont be set yet
            scrollView?.contentSize = imageView.frame.size
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        // Add the subview we created programmtically when we load
        scrollView.addSubview(imageView)
        // Wrap in a url 
        imageURL = NSURL(string: DemoURL.Stanford)
    }
}
