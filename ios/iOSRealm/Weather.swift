//
//  Weather.swift
//  iOSRealm
//
//  Created by Niels Bokmans on 08/11/16.
//  Copyright © 2016 Niels Bokmans. All rights reserved.
//

import Foundation
import RealmSwift

class Weather : Object {
    dynamic var date: String?
    dynamic var forecast: String?
    dynamic var humidity: String?
    dynamic var wind: Wind?
    override var description : String { return "\(date!) - \(forecast!) - \(humidity!) - \(wind!.description)" }
}
	
class Wind: Object {
    dynamic var direction: String?
    dynamic var speed:String?
    override var description : String { return "(\(direction!), \(speed!))" }
}