//
//  BreakoutPhysics.swift
//  Breakout_Assignment6
//
//  Created by J L on 8/11/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import UIKit

class BreakoutPhysics: UIDynamicBehavior, UICollisionBehaviorDelegate {
    
    /* MARK: Gravity
    let gravity: UIGravityBehavior = {
        let gravity = UIGravityBehavior()
        return gravity
    }()*/
    
    // MARK: Collision behaviors
    // the ball shoulda collide with boundaries given
    private lazy var collider: UICollisionBehavior = {
        let collider = UICollisionBehavior()
        collider.collisionMode = .Boundaries
        collider.collisionDelegate = self
        return collider
    }()
    
    
    func addCollisionBoundary(path: UIBezierPath, named name: String) {
        collider.removeBoundaryWithIdentifier(name)
        collider.addBoundaryWithIdentifier(name, forPath: path)
    }

    // slide the boundary of the paddle boundary to keep up with the moving frame of the paddle
    func slidePaddleViewBoundaryToMatchView(paddleView: UIView) {
        addCollisionBoundary(UIBezierPath(rect: paddleView.frame), named: BreakoutUIView.BoundaryNames.PaddleBoundaryName)
    }
    
    // array of bricks to remove visually from view
    var bricks = [UIView]()
    
    func collisionBehavior(behavior: UICollisionBehavior, beganContactForItem item: UIDynamicItem, withBoundaryIdentifier identifier: NSCopying?, atPoint p: CGPoint) {
        if let boundaryName = identifier as? String {
            if boundaryName != BreakoutUIView.BoundaryNames.PaddleBoundaryName && boundaryName != BreakoutUIView.BoundaryNames.BreakoutViewEdgesName { // if its a brick named by its number as a product of its row and column
                behavior.removeBoundaryWithIdentifier(boundaryName) // remove collision boundary
                if let boundaryNameAsInt = Int(boundaryName) {
                    let brick = bricks[boundaryNameAsInt]
                    UIView.animateWithDuration(0.3, animations: {
                        brick.backgroundColor = UIColor.redColor() // flash red
                        }, completion: { (success) in
                            if success {
                                brick.removeFromSuperview() // finally remove from view
                            }
                    })
                }
                itemBehavior.addAngularVelocity(-5, forItem: item)
            }
        }
        /* change balls velocity if its getting slow
        if itemBehavior.linearVelocityForItem(item).y < initialLinearVelocityOfBall.y {
            let newLinearVelocity = CGPoint(x: itemBehavior.linearVelocityForItem(item).x, y: itemBehavior.linearVelocityForItem(item).y + (initialLinearVelocityOfBall.y - itemBehavior.linearVelocityForItem(item).y))
            itemBehavior.addLinearVelocity(newLinearVelocity, forItem: item)
        }*/
        itemBehavior.addAngularVelocity(10, forItem: item)
    }

    
    // MARK: Ball behaviors
    // will let a velocity be set on the ball
    var initialLinearVelocityOfBall = CGPoint(x: 150, y: 750)
    
    private let itemBehavior: UIDynamicItemBehavior = {
        let itemBehavior = UIDynamicItemBehavior()
        itemBehavior.elasticity = 1.0
        itemBehavior.allowsRotation = false
        return itemBehavior
    }()
    
    func addBall(ball: UIDynamicItem) {
        collider.addItem(ball)
        itemBehavior.addItem(ball)
        itemBehavior.addLinearVelocity(initialLinearVelocityOfBall, forItem: ball)
        //gravity.addItem(ball)
    }
    
    
    
    // MARK: Init
    override init() {
        super.init()
        addChildBehavior(collider)
        addChildBehavior(itemBehavior)
        //addChildBehavior(gravity)
    }
}
