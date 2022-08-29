//
//  BreakoutGameViewController.swift
//  Breakout_Assignment6
//
//  Created by J L on 8/11/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import UIKit

class BreakoutGameViewController: UIViewController {
    
    @IBOutlet weak var breakoutGameView: BreakoutUIView! {
        didSet {
            breakoutGameView.gameDifficulty = .Normal
            breakoutGameView.animating = true
            breakoutGameView.addGestureRecognizer(UIPanGestureRecognizer(target: breakoutGameView, action: #selector(breakoutGameView.movePaddle(_:))))
        }
    }
    
    // when going to tvc settings tab, stop animating and start again by TODO: capturing values like linear velocity
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        breakoutGameView.animating = false
    }
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        breakoutGameView.animating = true
    }
}
