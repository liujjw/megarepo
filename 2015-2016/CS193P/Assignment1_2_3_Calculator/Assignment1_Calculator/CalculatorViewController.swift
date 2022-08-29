//
//  ViewController.swift
//  Assignment1_Calculator
//
//  Created by J L on 6/21/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

/* Single view controller for calculator that maps buttons and the display. Interacts with a "brain" class that acts as the model.
 
 Note: Does NOT support negative numbers.
 */

import UIKit

class CalculatorViewController: UIViewController, UISplitViewControllerDelegate {
    
    // Prevents empty graph from loading up first on iphone 
    override func viewDidLoad() {
        super.viewDidLoad()
        splitViewController?.delegate = self
    }
    // this method lets the split view's delegate
    // collapse the detail on top of the master when it's the detail's time to appear
    // this method returns whether we (the delegate) handled doing this
    // we don't want an empty detail to collapse on top of our master
    // so if the detail is an empty ImageViewController, we return true
    // (which tells the split view controller that we handled the collapse)
    // of course, we didn't actually handle it, we did nothing
    // but that's exactly what we want (i.e. no collapse if the detail ivc is empty)
        // -CS193P Instructor
    func splitViewController(splitViewController: UISplitViewController, collapseSecondaryViewController secondaryViewController: UIViewController, ontoPrimaryViewController primaryViewController: UIViewController) -> Bool {
        if primaryViewController.contentViewController == self {
            if let gvc = secondaryViewController.contentViewController as? GraphViewController where gvc.modelToGraph == nil {
                return true
            }
        }
        return false
    }
    
    // MARK: Model
    private var model = CalculatorBrain()
    
    
    // MARK: View -- Interacts with the various buttons on the calculator view.
    @IBOutlet weak var descriptionTextBinder: UILabel!
    @IBOutlet weak private var displayBinder: UILabel!
    private var displayText: String {
        get {
            return displayBinder.text!
        }
        set {
            displayBinder.text = newValue
        }
    }
    

    private var userIsInTheMiddleOfTyping = false // appends values if user is already typing a number (ie when true ) and clears display if user is just starting to type a new number (ie false)
    private var canUseDecimalPoint = true
    private var canUseDecimalPoint2 = true // does not allow use of decimal point when inputting variables, once its not a variable, allow it, canUseDecimalPoint original still works as usual, just with added aforementioned condition
    
    // ONLY deals with the view, does not interface with the model.
    @IBAction private func buttonPress(sender: UIButton) {
        if sender.currentTitle! == "." {
            if canUseDecimalPoint && canUseDecimalPoint2 { // ignore otherwise
                displayText = displayText + sender.currentTitle!
                canUseDecimalPoint = false
            }
        } else if Double(sender.currentTitle!) == nil { // checks if its M, which is treated like its own value, clear display if other numbers
            displayText = sender.currentTitle!
            userIsInTheMiddleOfTyping = false
            canUseDecimalPoint2 = false
        } else {
            canUseDecimalPoint2 = true
            if userIsInTheMiddleOfTyping {
                let textAlreadyInDisplay = displayText
                displayText = textAlreadyInDisplay + sender.currentTitle!
            } else {
                displayText = sender.currentTitle!
            }
            userIsInTheMiddleOfTyping = true // regardless of which condition, now user is in the middle of typing becuase they just pressed a button
        }
    }
    
    
    // Only this interacts with the model.
    @IBAction private func operatorPress(sender: UIButton) {
        if sender.currentTitle! == "←" { // tells model to backspace or undo depending on if user is in the middle of typing
            if userIsInTheMiddleOfTyping {
                model.isBackspace = true
                if Double(displayText) == nil { // still operand for backspace tho we need it
                    model.setOperand(displayText)
                } else {
                    model.setOperand(Double(displayText)!)
                }
            } else {
                model.isBackspace = false
            }
        } else { // set operand only if we are not gonna undo cuz it messes everything up by adding a duplicate operand
            if Double(displayText) == nil { // can be a variable, or a regular number
                model.setOperand(displayText)
            } else {
                model.setOperand(Double(displayText)!)
            }
        }
        model.performOperation(sender.currentTitle!) // perform operation on above, if unary then we have result, else, if binary we'll just store it until next operand is given
        displayText = model.returnResultInAccumulatorAsString()
        descriptionTextBinder.text = model.getDescription() // show result and description when another operator is presses or is equals sign
        if Double(displayText) == nil || sender.currentTitle != "←" { userIsInTheMiddleOfTyping = false } // we will clear display for next value if its a variable or if we used some operator other than backspace
        if displayText == "0" || sender.currentTitle!.hasPrefix("→") { userIsInTheMiddleOfTyping = false } // clear display if its just 0 or if we set a variable
    }
    
    // →M sets the value of the variable M in the brain to the current value of the display and then shows the brain’s result in the display given the new variable value.
    @IBAction func setVariableM() {
        if let _ = Double(displayText) { // this will be true most of the time, but sometimes user sets variable to itself so well just ignore it then
            model.setVariable("M", variableValue: Double(displayText)!) // first set the value of the specific variable
             // sets the property observer in model to re evaulate an expression stored with this variable if there is one
            // may not be clear that we are recalculating any results
            displayText = model.returnResultInAccumulatorAsString() // displayText will have a new result maybe
            userIsInTheMiddleOfTyping = false // clear
        }
    }
    /* // more general code for indexing variable names in buttons
     let indexOfButtonVariableName = sender.currentTitle!.startIndex.successor()
     let variableNameFromButton = String(sender.currentTitle![indexOfButtonVariableName])
     var valueSetToVariable = 0.0
     valueSetToVariable = model.getValueForVariable(variableNameFromButton)
     model.setVariable(variableNameFromButton, variableValue: valueSetToVariable)*/
    
    /* Binds to clear button that resets the state of everything. */
    @IBAction private func clearButton(sender: UIButton) {
        model.reset()
        userIsInTheMiddleOfTyping = false
        canUseDecimalPoint = true
        displayText = "0"
        descriptionTextBinder.text = " "
    }
    
    /* For saving and restoring a program from memory, useful for something like variables for exmple.*/
    var savedProgram: CalculatorBrain.PropertyList?
    @IBAction func save() {
        savedProgram = model.program
    }
    @IBAction func restore() {
        if savedProgram != nil {
            model.program = savedProgram!
            displayText = model.returnResultInAccumulatorAsString()
        }
    }
    
    
    // MARK: - Navigation for segueing to a graph view and preparing it talking to its controller. 
    // The Calculator View Controller deals with the creation of the Graph View Controller. It gives the new MVC the data, ie the eqaution, for its Model and also the title of it in the navigation controller.
    // NOTE: http://stackoverflow.com/questions/10267677/cs193p-assignment-3-hint5-what-is-the-model
    // So...passin the program array as the function, but we can't use the calculator brain API to calculate values of the function.
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // First isolate the view controller in the navigation controller segueing to
        var destinationVC = segue.destinationViewController
        if let destinationNavCon = destinationVC as? UINavigationController {
            destinationVC = destinationNavCon.visibleViewController ?? destinationNavCon
        }
        
        if let identifier = segue.identifier { // identifier to have specific interactions with specific segues
            switch identifier {
            case "Show Graph":
                // ie take the expression in the display, guatanteed by the shouldPerformSegue... function to not be partial, give it to the graph view controller 
                // "should graph the program in the calculator brain" [at the time of segue]
                if let graphVC = destinationVC as? GraphViewController {
                    graphVC.modelToGraph = model
                    graphVC.navigationItem.title = descriptionTextBinder.text
                }
            default: break
            }
        }
    }
    
    
    // Do not perform the segue if there is a partial result to graph.
    override func shouldPerformSegueWithIdentifier(identifier: String, sender: AnyObject?) -> Bool {
        if displayText.hasSuffix("...") { // ie partial result
            return false
        } else {
            return true
        }
    }
    
    
}

// CS193P Instructor
extension UIViewController {
    var contentViewController: UIViewController {
        if let navcon = self as? UINavigationController {
            return navcon.visibleViewController ?? self
        } else {
            return self
        }
    }
}

