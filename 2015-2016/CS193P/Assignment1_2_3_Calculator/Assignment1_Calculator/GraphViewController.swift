//
//  GraphViewController.swift
//  Assignment1_Calculator
//
//  Created by J L on 7/11/16.
//  Copyright © 2016 Jackie Liu. All rights reserved.
//

import UIKit

class GraphViewController: UIViewController {
    
    
    // MARK: Model
    // TODO: Hints suggest we turn it into a closure somehwere to pass to graph view
    // however im gonna try passing the brain itself
    var modelToGraph: CalculatorBrain? {
        didSet {
            detailGraph?.functionToGraph = modelToGraph
        }
    }

    // MARK: View

    // Center the graph whenever the view is laid out, ie the first time or when rotation occurs.
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        detailGraph.graphOrigin = CGPoint(x: detailGraph.bounds.midX, y: detailGraph.bounds.midY)
    }
    // Pointer to the view graph and add some gestures to it.
    @IBOutlet weak var detailGraph: GraphView! {
        didSet {
            detailGraph?.functionToGraph = modelToGraph
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        detailGraph.addGestureRecognizer(
            UIPanGestureRecognizer(
                target: detailGraph, action: #selector(detailGraph.pan(_:))
            )
        )
        detailGraph.addGestureRecognizer(
            UIPinchGestureRecognizer(
                target: detailGraph, action: #selector(detailGraph.changeScale(_:))
            )
        )
        let doubleTapRecognizer = UITapGestureRecognizer(target: detailGraph, action: #selector(detailGraph.moveOrigin(_:)))
        doubleTapRecognizer.numberOfTapsRequired = 2 // Double tap
        doubleTapRecognizer.numberOfTouchesRequired = 1
        detailGraph.addGestureRecognizer(doubleTapRecognizer)
    }
    
}
