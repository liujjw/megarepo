// Give me loan amount, APR, and monthly payment
// I will give you number of months it will take your loan to be paid in full.

import UIKit

var counter = 0
let monthlyPayment: Double = 562
let APR_Percent: Double = 0.05
let APRMonthly_Percent: Double = APR_Percent / 12
let loanAmount: Double = 125000
let loanCompoundingRatio_Monthly = 1 + APRMonthly_Percent

func calculate(expressionValue: Double) {
    if expressionValue <= 0 {
        counter += 1
        return
    }
    calculate((expressionValue * loanCompoundingRatio_Monthly) - monthlyPayment)
    counter += 1
}
calculate((loanAmount*loanCompoundingRatio_Monthly) - monthlyPayment)
print("\(counter) months to pay off ")
