//
//  CalculatorBrain.swift
//  Assignment1_Calculator
//
//  Created by J L on 6/21/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

/*
 * NOTES: Does NOT support negative values.
 * TODO: Clean up unary operations like trig and sqrt into one type
 * TODO: Support for error checking as in assignment spec: Have your calculator report errors. For example, the square root of a negative number or divide by zero. There are a number of ways to go about “detecting” these errors (maybe add an associated value to the Unary/BinaryOperation cases which is a function that detects an error or perhaps have the function that is associated with a Unary/BinaryOperation return a tuple with both the result and an error (if any)
    or ???). How you report any discovered errors back to users of the CalculatorBrain API will require some API design on your part, but don’t force users of the CalculatorBrain API to deal with errors if they don’t want to (i.e. allow Controllers that want to display errors to do so, but let those that don’t just deal with NaN and +∞ appearing in their UI). In other words, don’t change any of the existing methods or properties in the non-private API of CalculatorBrain to support this feature (add methods/properties as needed instead).
 * TODO: Descrption building is a complete fcking mess in perform operation code.
 */

import Foundation

class CalculatorBrain {
    
    // eg display shows M (a variable), so accumulator will hold its value along with its string representation too, though it is optional
    private struct accumulatorValueAndVar {
        var value: Double
        var variable: String?
    }
    private var accumulator = accumulatorValueAndVar(value: 0.0, variable: nil) // accumulates the internal value like a real calculator
    
    
    private var internalProgram = [AnyObject]() // stores a program for use later
    
    
    private var internalProgramToUndo = [AnyObject]() // for undo functinality, separate for clarity
    var isBackspace = false // client tells us whether to undo or backspace based on userIsInTheMiddleOfTyping
    
    
    
    // This can be encapsulated into a struct and then array of struct for multiple distinct variables at once POSSIBLY.
    private var internalVariableExpressionM = [AnyObject]() // stores an expression with a variable so it can be reevaluated later when the variable is set
    private var isVarInExpressionM = false // dont evalaute if a variable wasnt put into the expression
    private var variableInExpressionM: Double = 0.0 { // stores variable's value in expression and once set, the expression will be re-evaluated
        willSet {
            if isVarInExpressionM {
                let internalStates = saveStateOfProperties()
                let ops = internalVariableExpressionM // get a copy of expression, see performOperation as to why
                reevaluateExpression(ops, dontDo: nil)
                internalProgram = internalStates.internalProgram
                internalProgramToUndo = internalStates.toUndo
                internalVariableExpressionM = internalStates.expressionM
                description = internalStates.description
            }
        }
    }
    
    // includes an optional value that will make it such that only a set number of the operations are performed, for undo for example, if nil we do everything
    private func reevaluateExpression(expression: [AnyObject], dontDo: Int?) {
        var isPreviousAnEqualsSign = false // if the previous operand/operation was an equals sign in our expression, that means that result is the result of the expression with M not yet set so it is wrong, we insted will set the result after the equals sign to the accumulator which will hold the correct value after setting the varaible and evaluating it up until the equal sign
        var operationsDone = 0
        for op in expression { // reuse our previous facilties and run it like its being typed into the display
            if dontDo != nil { // for undo, dont do the last dontDo operations
                if operationsDone >= expression.count - dontDo! { break }
            }
            if let operand = op as? Double { // if a regular number
                if isPreviousAnEqualsSign {
                    setOperand(accumulator.value) // this means that this number operand is the result of the calculation of M where it was not yet set, therefore since M is now set, accumualtor value stores that result calculating the set varaible and should instead be the operand
                    isPreviousAnEqualsSign = false
                } else {
                    setOperand(operand)
                }
            } else if let variableOrOperator = op as? String { // can either be a variable or a string operator, we handle this
                if let _ = operatorToFunction[variableOrOperator] { // if an operator that can be found, given that no expression will have the same name as an opreator
                    performOperation(variableOrOperator)
                    if variableOrOperator == "=" { isPreviousAnEqualsSign = true }
                } else { // if a variable
                    let value = variableValues[variableOrOperator]!
                    setOperand(value)
                }
            }
            operationsDone += 1
        }

    }
    
    // call this to save the state of the internal programs because they will get changed whenever we reevaluate expresssions in them
    private func saveStateOfProperties() -> (
        expressionM: [AnyObject],
        internalProgram: [AnyObject],
        toUndo: [AnyObject],
        description: String
        ){
        return (
            internalVariableExpressionM,
            internalProgram,
            internalProgramToUndo,
            description
        )
    }
    
    
    
    private var description = "" // returns description of sequence of operands and operators leading to result

    
    
    
    /* Data structures underlying our main calc operations. */
    private enum OperatorType {
        case Trig((Double) -> Double)
        case Constants(Double)
        case BinaryOperation((Double, Double) -> Double)
        case SquareRoot((Double) -> Double)
        case Equals
        case BackspaceUndo
        case Random(() -> UInt32)
    }
    private var operatorToFunction: Dictionary<String, OperatorType> = [
        "+" : OperatorType.BinaryOperation({ $0 + $1 }),
        "-" : OperatorType.BinaryOperation({ $0 - $1 }),
        "✕" : OperatorType.BinaryOperation({ $0 * $1 }),
        "÷" : OperatorType.BinaryOperation({ $0 / $1 }),
        "sin" : OperatorType.Trig(sin), // radians
        "cos" : OperatorType.Trig(cos),
        "tan" : OperatorType.Trig(tan),
        "π" : OperatorType.Constants(M_PI),
        "x^y" : OperatorType.BinaryOperation(pow),
        "√" : OperatorType.SquareRoot(sqrt),
        "=" : OperatorType.Equals,
        "←" : OperatorType.BackspaceUndo,
        "rand" : OperatorType.Random(arc4random)
    ]
    
    
    
    /* API to store and retrieve variables*/
    private var variableValues: Dictionary<String, Double> = [:] // allows client to set a new var in the dict
    func setVariable(variableName: String, variableValue: Double) {
        variableValues[variableName] = variableValue
        isVarInExpressionM = true
        variableInExpressionM = variableValue // variable is set client side, might have an expression to reevaluate
    }
    // returns value associated with a variable, defaults to 0.0
    func getValueForVariable(variableName: String) -> Double {
        if variableValues[variableName] == nil {
            return 0.0
        } else {
            return variableValues[variableName]!
        }
    }

    
    
    // Will be called first when an operator is pressed, just keeps track of the value in the display into the accumulator
    func setOperand(operand: Double) {
        accumulator.value = operand
        accumulator.variable = nil
        internalProgram.append(operand)
        internalVariableExpressionM.append(operand)
        internalProgramToUndo.append(operand)
    }
    // overloaded to take string variables instead, but underlying representation is just numeric still
    func setOperand(variableName: String) {
        accumulator.value = getValueForVariable(variableName)
        accumulator.variable = variableName
        internalProgram.append(variableName)
        internalProgramToUndo.append(variableName) // set the variable so we know new value if it changes because when evaluting we will look it up
        internalVariableExpressionM.append(variableName)
        isVarInExpressionM = true
    }
    
    
    
    
    private var isPartialResult = false // whether there is a pending binary operation
    private struct pendingBinaryOperation { // keeps data until given a second operand to a binary operation
        var firstOperand: Double
        var operation: (Double, Double) -> Double
    }
    private var pending: pendingBinaryOperation? = nil
    
    
    
    
    func performOperation(calcOperator: String) {
        if calcOperator != "←" {
            internalProgram.append(calcOperator)
            internalVariableExpressionM.append(calcOperator) // wont this keep adding operations to inifinty to our anyobject arrays when we evalute them? NO because we used optional binding to get a copy at that state, which does not get updated with the original, which will have to be reset each time we are done evaluating
            internalProgramToUndo.append(calcOperator)
        } // do not want ← backsapce/undo to be added to our internal expressions cuz undo is its own thing
        if let operation = operatorToFunction[calcOperator] {
            switch operation {
            case .Constants(let value):
                accumulator.value = value
            case .Trig(let function):
                updateDescriptionForUnaryOperator(calcOperator)
                accumulator.value = function(accumulator.value)
            case .SquareRoot(let function):
                updateDescriptionForUnaryOperator(calcOperator)
                accumulator.value = function(accumulator.value)
            case .BinaryOperation(let function):
                if pending == nil {
                    pending = pendingBinaryOperation(firstOperand: accumulator.value, operation: function)
                    isPartialResult = true
                    if isVarInExpressionM { // if there's a variable we do different things to the description by keping the remainign expression
                        if description == "" { // add both the accumulator value and the operator if there is an empty description
                            if accumulator.variable != nil { // if accumulator has an associated variable put that instead
                                description = "\(accumulator.variable!)\(calcOperator)"
                            } else {
                                description = "\(pending!.firstOperand)\(calcOperator)"
                            }
                        } else {
                            description += "\(calcOperator)" // only add the operator if there is already an expression on the screen that has variables
                        }
                    } else {
                        description = "\(pending!.firstOperand)\(calcOperator)" // else reset description
                    }
                } else {
                    if accumulator.variable != nil {
                        description += "\(accumulator.variable!)\(calcOperator)"
                    } else {
                       description += "\(accumulator.value)\(calcOperator)" // before evaulating pending operation, add in the current display value/ accumualtor value then the operation
                    }
                    accumulator.value = pending!.operation(pending!.firstOperand, accumulator.value) // eg 5 * 5 then press this
                    pending = pendingBinaryOperation(firstOperand: accumulator.value, operation: function) // eg 5 * 5 is 25, then user presses a + operator so different pending operation now
                    isPartialResult = true
                }
            case .Equals:
                if pending != nil {
                    if accumulator.variable != nil { // if accumulator has an associated variable put that instead
                        description += "\(accumulator.variable!)"
                    } else {
                        let lastOperand = CalculatorBrain.checkIfConstant(accumulator.value) // for example 3.14.. should show π
                        description += "\(lastOperand)" // add final operand before calculation
                    }
                    accumulator.value = pending!.operation(pending!.firstOperand, accumulator.value) // execute final pending op
                    isPartialResult = false // reset
                    pending = nil
                }
            case .BackspaceUndo:
                if isBackspace {
                    backspace()
                } else {
                    undo()
                }
            case .Random(let function):
                accumulator.value = Double(function())
            }
        }
    }
    private func backspace() { // backspace side effect is change of accumualtor.value
        if accumulator.variable != nil { // handling of a varaible in display
            accumulator.variable = nil
            accumulator.value = 0.0
        } else {
            var numberAsString = ""
            // 36 is given as 36.0, so cast to rid it
            if ((accumulator.value - Double(Int(accumulator.value)) == 0)) { numberAsString = String(Int(accumulator.value)) }
            else { numberAsString = String(accumulator.value) }
            numberAsString.removeAtIndex(numberAsString.endIndex.predecessor())
            if numberAsString.startIndex == numberAsString.endIndex { accumulator.value = 0.0 }
            else { accumulator.value = Double(numberAsString)! }
        }
    }
    private func undo() {
        description = "" // make it empty before saving its state
        let internalStates = saveStateOfProperties()
        var undoExpression = internalProgramToUndo
        accumulator.value = 0.0 // defaults at 0, so if we undo two oeprtions nothing is done, then acccumulator is auto 0
        pending = nil // default
        reevaluateExpression(undoExpression, dontDo: 3) // dont do the last 3 operations
        for _ in 1...3 { // truncate the undoExpression to not contain the last two operations
            if undoExpression.count == 0 {
                break
            } else {
                undoExpression.removeLast()
            }
        }
        internalProgram = internalStates.internalProgram
        
        internalProgramToUndo = undoExpression // truncated expression is what we want, not internal state saved
        
        internalVariableExpressionM = internalStates.expressionM
        // dont reset the description from "" above, no duplicate description should occur
    }
    /* Helpers for performOperation */
    private func updateDescriptionForUnaryOperator(calcOperator: String) {
        if isPartialResult { // 7 + 10 √, only apply to 10, know when its a partial result
            // if theres varialbe put that instead
            description = accumulator.variable == nil ? description + (calcOperator + "(\(accumulator.value))") : description + (calcOperator + "(\(accumulator.variable!))")
        } else {
            // if there variable
            if accumulator.variable == nil {
                // if description is empty at first put in whats in the accumulator
                description = description == "" ? calcOperator + "(\(accumulator.value))" : calcOperator + "(\(description))" // 7 + 10 = 17 √ apply to 7 + 10
            } else {
                description = description == "" ? calcOperator + "(\(accumulator.variable!))" : calcOperator + "(\(description))" // 7 + 10 = 17 √ apply to 7 + 10
            }
        }
    }
    static private func checkIfConstant(value: Double) -> String {
        switch value {
        case 3.14159265358979: // using M_PI does not work
            return "π"
        default:
            return String(value)
        }
    }
    
    
    
    
    /* Captures an expression for use later. */
    typealias PropertyList = AnyObject
    var program: PropertyList {
        get {
            return internalProgram // value type copy
        } set {
            reset() // for next use, clear everything
            if let arrayOfOps = newValue as? [AnyObject] { // must be anyobject else ignore
                reevaluateExpression(arrayOfOps, dontDo: nil)
            }
        }
    }
    
    
    
    
    func getDescription() -> String {
        if isPartialResult {
            return description + " ... "
        } else {
            return description + " = "
        }
    }
    
    
    
    
    
    func returnResultInAccumulatorAsString() -> String {
        // if 36, then displays 36.0, we want to remove that .0 if its just a whole number
        if (accumulator.value - Double(Int(accumulator.value)) == 0) {
            return String(Int(accumulator.value))
        } else {
            return String(accumulator.value)
        }
    }
    
    
    
    
    func reset() {
        accumulator.value = 0.0; accumulator.variable = nil
        pending = nil
        description = ""
        internalVariableExpressionM.removeAll()
        internalProgram.removeAll()
        variableValues.removeAll()
        internalProgramToUndo.removeAll()
        isVarInExpressionM = false
    }
    
}

