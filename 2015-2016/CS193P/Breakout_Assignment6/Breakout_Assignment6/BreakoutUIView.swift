//
//  BreakoutUIView.swift
//  Breakout_Assignment6
//
//  Created by J L on 8/11/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import UIKit

class BreakoutUIView: UIView {
    
    // MARK: Internal constants for boundary names
    struct BoundaryNames {
        static let PaddleBoundaryName = "Paddle"
        static let BreakoutViewEdgesName = "Bounds"
    }
    
    // MARK: Difficulty
    enum Difficulty {
        case Easy
        case Normal
        case Hard
    }
    
    // when changed, game settings change based on it
    var gameDifficulty: Difficulty? {
        didSet {
            settings = difficultyForGameSettings[gameDifficulty!]
            setNeedsDisplay()
        }
    }
    
    private let difficultyForGameSettings: [Difficulty:GameSettings] = [
        Difficulty.Easy:GameSettings(numberOfRowsOfBricks: 10, numberOfColumnsOfBricks: 4),
        Difficulty.Hard:GameSettings(numberOfRowsOfBricks: 15, numberOfColumnsOfBricks: 16),
        Difficulty.Normal:GameSettings(numberOfRowsOfBricks: 12, numberOfColumnsOfBricks: 8)
    ]
    
    
    // MARK: Game settings and constants based on difficulty
    private struct GameSettings {
        var numberOfRowsOfBricks: Int
        var numberOfColumnsOfBricks: Int
    }
    
    private var settings: GameSettings?
    
    // MARK: Animator
    private lazy var animator: UIDynamicAnimator = {
        let animator = UIDynamicAnimator(referenceView: self)
        return animator
    }()
    
    private var physics = BreakoutPhysics()
    
    var animating: Bool = false {
        didSet {
            if animating {
                animator.addBehavior(physics)
            } else {
                animator.removeBehavior(physics)
            }
        }
    }
    
    // MARK: drawRect
    override func drawRect(rect: CGRect) {
        drawPaddle()
        drawBall()
        drawBricks()
        let leftRightTopBoundaries = UIBezierPath()
        leftRightTopBoundaries.moveToPoint(CGPoint(x: bounds.minX, y: bounds.maxY))
        leftRightTopBoundaries.addLineToPoint(CGPoint(x: bounds.minX, y: bounds.minY))
        leftRightTopBoundaries.addLineToPoint(CGPoint(x: bounds.maxX, y: bounds.minY))
        leftRightTopBoundaries.addLineToPoint(CGPoint(x: bounds.maxX, y: bounds.maxY))
        physics.addCollisionBoundary(leftRightTopBoundaries, named: BoundaryNames.BreakoutViewEdgesName)
    }
    
    private func drawBricks() {
        if settings != nil {
            let offset: CGFloat = 1
            let heightOfBrick: CGFloat = (bounds.height * 0.02)
            let widthOfBrick: CGFloat = (bounds.width / CGFloat(settings!.numberOfColumnsOfBricks)) - offset
            for row in 0..<settings!.numberOfRowsOfBricks {
                for column in 0..<settings!.numberOfColumnsOfBricks {
                    let frame = CGRect(origin: CGPoint(x: CGFloat(column) * (widthOfBrick + offset), y: CGFloat(row) * (heightOfBrick + offset)), size: CGSize(width: widthOfBrick, height: heightOfBrick))
                    let brick = UIView(frame: frame)
                    brick.backgroundColor = UIColor.blueColor()
                    addSubview(brick)
                    // bricks can be referenced by their number added together
                    // number if calculated to account for the brick's number as the order in which
                    // it was made
                    let brickNameAsNumber = (row * settings!.numberOfColumnsOfBricks) + column
                    physics.addCollisionBoundary(UIBezierPath(rect: frame), named: "\(brickNameAsNumber)")
                    physics.bricks.append(brick)
                }
            }
        }
    }
    
    private func drawBall() {
        let ball = UIView()
        
        let ballSize = CGSize(width: bounds.width / 35, height: bounds.width / 35)
        let ballOrigin = CGPoint(x: (bounds.width - ballSize.width) / 2, y: (bounds.height - ballSize.height) / 2)
        
        ball.frame = CGRect(origin: ballOrigin, size: ballSize)
        ball.backgroundColor = UIColor.redColor()
        
        self.addSubview(ball)
        physics.addBall(ball)
    }
    
    private func drawPaddle() {
        let paddle = UIView()
        
        let paddleSize = CGSize(width: bounds.width / 6, height: (bounds.width / 5) * 0.2)
        let paddleOrigin = CGPoint(x: (bounds.width - paddleSize.width) / 2, y: (bounds.height - paddleSize.height - bounds.height * 0.05))
        
        paddle.frame = CGRect(origin: paddleOrigin, size: paddleSize)
        paddle.backgroundColor = UIColor.blackColor()
        
        self.addSubview(paddle)
        
        physics.addCollisionBoundary(UIBezierPath(ovalInRect: paddle.frame), named: BoundaryNames.PaddleBoundaryName)
        paddleView = paddle
    }
    
    private var paddleView: UIView?
    
    func movePaddle(recognizer: UIPanGestureRecognizer) {
        if paddleView != nil {
            switch recognizer.state {
            case .Ended, .Changed:
                let translation = recognizer.translationInView(self)
                // translation must not take paddle out of bounds
                if !((paddleView!.frame.origin.x + translation.x) < self.bounds.minX) && !(paddleView!.frame.origin.x + paddleView!.frame.width + translation.x > self.bounds.maxX) {
                    paddleView!.center.x += translation.x
                    physics.slidePaddleViewBoundaryToMatchView(paddleView!)
                }
                recognizer.setTranslation(CGPointZero, inView: self)
            default: return
            }
        }
    }

}
