//
//  CalculatorModel.swift
//  Calculator
//
//  Created by J L on 6/17/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import Foundation

class CalculatorModel
{
    private var accumulator = 0.0
    
    func setOperand(operand: Double) {
        accumulator = operand
    }
    
    private var operations: Dictionary<String,Operation> = [
        "π" : Operation.Constant(M_PI),
        "e" : Operation.Constant(M_E),
        "±" : Operation.UnaryOperations({ -$0 }),
        "√" : Operation.UnaryOperations(sqrt),
        "cos" : Operation.UnaryOperations(cos),
        "✕" : Operation.BinaryOperations({ $0 * $1 }),
        "÷" : Operation.BinaryOperations({ $0 / $1 }),
        "+" : Operation.BinaryOperations({ $0 + $1 }),
        "-" : Operation.BinaryOperations({ $0 - $1 }), // closures
        "=" : Operation.Equals
    ]
    
    private enum Operation {
        case Constant(Double)
        case UnaryOperations((Double) -> Double) // generic function takes a double and returns a double
        case BinaryOperations((Double,Double) -> Double)
        case Equals
    }
    
    func performOperation(symbol: String) {
        if let operation = operations[symbol] {
            switch operation {
            case .Constant(let value):
                accumulator = value
            case .UnaryOperations(let function):
                accumulator = function(accumulator)
            case .BinaryOperations(let function):
                executePendingBinaryOperation()
                pending = PendingBinaryOperationInfo(binaryFunction: function, firstOperand: accumulator)
            case .Equals:
                executePendingBinaryOperation()
            }
        }
    }
    
    private func executePendingBinaryOperation() {
        if pending != nil {
            accumulator = pending!.binaryFunction(pending!.firstOperand, accumulator)
            pending = nil
        }
    }
    
    private var pending: PendingBinaryOperationInfo?
    
    private struct PendingBinaryOperationInfo { // biggest difference is that structs and enums are pass by value compared to classes pass by reference, eg double, strings, arrays are all structs so pass by value
        var binaryFunction: (Double, Double) -> Double // no initlizer bc every var has one
        var firstOperand: Double
    }
    
    var result: Double {
        get {
            return accumulator
        }
    }
}