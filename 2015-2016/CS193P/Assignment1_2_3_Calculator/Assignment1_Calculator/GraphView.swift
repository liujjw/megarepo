//
//  GraphView.swift
//  Assignment1_Calculator
//
//  Created by J L on 7/11/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

// TODO: 4. Figure out how to use Instruments to analyze the performance of panning and pinching in your graphing view. What makes dragging the graph around so sluggish? Explain in comments in your code what you found and what you might do about it.
//  5. Use the information you found above to improve panning performance. Do NOT turn your code into a mess to do this. Your solution should be simple and elegant. There is a strong temptation when optimizing to sacrifice readability or to violate MVC boundaries, but you are NOT allowed to do that for this Extra Credit!
import UIKit

@IBDesignable
class GraphView: UIView {
    
    
    // MARK: Public API.
    
    @IBInspectable
    var graphAxesColor: UIColor = UIColor.blueColor() { didSet { setNeedsDisplay() } }
    @IBInspectable
    var graphScale: CGFloat = 100 { didSet { setNeedsDisplay() } }
    @IBInspectable
    var graphOrigin: CGPoint = CGPoint(x: 0, y: 0) { didSet { setNeedsDisplay() } }
    
    
    // MARK: "Model" to draw and get our graph coords
    
    var functionToGraph: CalculatorBrain?
    // The function that takes an x coordinate to get a matching y. X is NOT in terms of the graph bounds and neither is y. Based off the function we are given to graph.
    private func computeXForY(x: CGFloat) -> CGFloat {
        if let functionModel = functionToGraph {
            functionModel.setVariable("M", variableValue: Double(x))
            return CGFloat(Double(functionModel.returnResultInAccumulatorAsString())!)
        }
        print("function was not set")
        return 0
    }
    
    // MARK: Drawing code
    private struct PointRightOrLeftOfOrigin {
        // Points for plotting actual locations to graph and points for calculating the y.
        var xCoordinateAsPointsToGraphOnTheBounds: CGFloat
        var xCoordinateForCalculations: CGFloat
    }
    
    override func drawRect(rect: CGRect) {
        // AxesDrawer class made by CS193P Instructor. Draws axes.
        let xyAxes = AxesDrawer(color: graphAxesColor, contentScaleFactor: self.contentScaleFactor)
        xyAxes.drawAxesInRect( self.bounds, origin: graphOrigin, pointsPerUnit: graphScale)
        
        // Draws function lines and curves. Start at the origin, move left or right by the graphScale, which is the number of points between units(the next consecutive integer) on the graph, until you reach the bounds.
        // Actaully computing the values uses integer-esque format, but MUST add into account the actual scale and bounds of drawing. Also since origin starts at the top and moving down means adding to y instead of subtracting, must take that into acount as well.
        let startingCoordinate = CGPoint(x: graphOrigin.x, y: graphOrigin.y + -(graphScale * computeXForY(0)))
        
        let rightLineToDraw = UIBezierPath()
        rightLineToDraw.moveToPoint(startingCoordinate)
        
        let leftLineToDraw = UIBezierPath()
        leftLineToDraw.moveToPoint(startingCoordinate)
        
        // Keeps track of x coordinates.
        var rightOfOrigin = PointRightOrLeftOfOrigin(
            xCoordinateAsPointsToGraphOnTheBounds: graphOrigin.x,
            xCoordinateForCalculations: 0)
        var leftOfOrigin = PointRightOrLeftOfOrigin(
            xCoordinateAsPointsToGraphOnTheBounds: graphOrigin.x,
            xCoordinateForCalculations: 0)
        
        while rightOfOrigin.xCoordinateAsPointsToGraphOnTheBounds <= bounds.width {
            rightOfOrigin.xCoordinateAsPointsToGraphOnTheBounds += 1
            rightOfOrigin.xCoordinateForCalculations += 1 / graphScale
            rightLineToDraw.addLineToPoint(CGPoint(
                x: rightOfOrigin.xCoordinateAsPointsToGraphOnTheBounds,
                y: graphOrigin.y + -(graphScale * (computeXForY(rightOfOrigin.xCoordinateForCalculations)))))
        }
        while leftOfOrigin.xCoordinateAsPointsToGraphOnTheBounds >= 0 {
            leftOfOrigin.xCoordinateAsPointsToGraphOnTheBounds -= 1
            leftOfOrigin.xCoordinateForCalculations -= 1 / graphScale
            leftLineToDraw.addLineToPoint(CGPoint(
                x: leftOfOrigin.xCoordinateAsPointsToGraphOnTheBounds,
                y: graphOrigin.y + -(graphScale * (computeXForY(leftOfOrigin.xCoordinateForCalculations)))))
        }
        
        leftLineToDraw.stroke()
        rightLineToDraw.stroke()
    }
    
    
    // MARK: Gesture handlers.
    // Pan should move the origin and therefore the graph.
    func pan(gesture: UIPanGestureRecognizer) {
        switch gesture.state {
        case .Changed: fallthrough
        case .Ended:
            let translation = gesture.translationInView(self)
            graphOrigin.x += translation.x
            graphOrigin.y += translation.y
            gesture.setTranslation(CGPointZero, inView: self)
        default: break
        }
    }
    // Pinch to scale the graph, ie zoom in and out.
    func changeScale(gesture: UIPinchGestureRecognizer) {
        switch gesture.state {
        case .Changed, .Ended:
            // Divide by the magnitude of the pinch: Pinch outward means larger scale, means we divide by that larger scale to get a smaller graphScale which means less units per point means larger numbers overall.
            let scale = gesture.scale
            graphScale /= scale
            gesture.scale = 1
        default: break
        }
    }
    
    // Double Tap to move to this point.
    func moveOrigin(gesture: UITapGestureRecognizer) {
        switch gesture.state {
        case .Ended:
            graphOrigin = gesture.locationInView(self)
        default: break
        }
    }

}
