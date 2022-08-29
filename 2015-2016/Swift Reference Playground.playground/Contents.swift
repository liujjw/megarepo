print("Hello, world")
var test = 09;
let test2: String = String(test);
print(test2)
print("I have a secret to tell you, and its not a good one because I'm actually \(test) years old and I didn't tell you")
var myArray = ["black paint", "avocadoes"]
print(myArray)
var dick = [
    "Malcolm" : "ass",
    "John" : "sass"
]

var myTest = [String]()
let emptyDick = [String: String]()
myTest = []

for randomString in myArray {
    if randomString == "dog shit" {
        print("yes")
    } else if randomString == "black paint" {
        print("found the rascist")
    }
}

var `var` = "dog"

print(`var`.characters.count)

typealias PropertyList = AnyObject

var myTuple = (vulgarStatement: "Dogs suck me!!", randomBooleanValue: true, randomYetAgain: myTest)
let (suckStatement, _, varOfAnArray) = myTuple
print("\u{1F496}")

if #available(iOS 9, *) {
    
}

enum rawEnumerationYay: Int {
    case dogs = 1
    case catz = 5
}

if let test = rawEnumerationYay(rawValue: 5) {
    print(test)
} else {
    print("nope")
}

enum arithmetic {
    case Number(Int)
    indirect case Addition(arithmetic, arithmetic)
    indirect case Multiplication(arithmetic, arithmetic)
}


        
