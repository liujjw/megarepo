//: Playground - noun: a place where people can play

import UIKit

private struct expressionWithVariable {
    var nonVariableValue: Double
    var variableValue: Double = 0 {
        willSet {
            print(newValue)
        }
    }
    var operation: (Double, Double) -> Double
}

private var adb = expressionWithVariable(nonVariableValue: 1, variableValue: 0, operation: {$0 * $1})

adb.variableValue = 9
adb.variableValue = 10

