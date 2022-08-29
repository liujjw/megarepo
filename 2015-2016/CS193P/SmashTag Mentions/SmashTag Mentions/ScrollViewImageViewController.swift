//
//  ScrollViewImageViewController.swift
//  SmashTag Mentions
//
//  Created by J L on 7/25/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import UIKit

class ScrollViewImageViewController: UIViewController, UIScrollViewDelegate {
    
    // Owns a scroll view that allows zooming of an image set by 
    // a segue. TODO: It first appears completely zoomed out in its native aspect ratio with as least white space as possible.
    

    @IBOutlet weak var scrollView: UIScrollView! {
        didSet {
            scrollView.delegate = self
            scrollView.minimumZoomScale = 0.35
            scrollView.maximumZoomScale = 3.5
            
            if let imageToAdd = image {
                let aspectRatio = (imageToAdd.size.width / imageToAdd.size.height)
                let topLeftOfScrollView = CGPoint(x: scrollView.bounds.minX, y: scrollView.bounds.minY)
                let screenBounds = UIScreen.main.bounds // using view.bounds causes a crash?
                imageView.frame = CGRect(origin: topLeftOfScrollView, size: CGSize(width: screenBounds.width, height: screenBounds.width / aspectRatio))
                
                scrollView.contentSize = imageView.frame.size
            }
        }
    }

    
    func viewForZooming(in scrollView: UIScrollView) -> UIView? {
        return imageView
    }
    // update the space we can scroll after zoom occurs
    func scrollViewDidEndZooming(_ scrollView: UIScrollView, with view: UIView?, atScale scale: CGFloat) {
        if let zoomedImageView = view as? UIImageView {
            if let zoomedImage = zoomedImageView.image {
                scrollView.contentSize = CGSize(width: zoomedImage.size.width * scale, height: zoomedImage.size.height * scale)
            }
        }
    }
    // image is like an intermediary between the imageview and its setup
    // doesnt actually store any data, its actually just there for its name
    // and clarity
    fileprivate var imageView = UIImageView()
    var image: UIImage? {
        get {
            return imageView.image
        } set {
            imageView.image = newValue
        }
    }

    // MARK: VCL
    override func viewDidLoad() {
        super.viewDidLoad()
        scrollView.addSubview(imageView)
    }
    

}
