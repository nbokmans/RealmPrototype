//
//  EditWeatherViewController.swift
//  iOSRealm
//
//  Created by Niels Bokmans on 08/11/16.
//  Copyright © 2016 Niels Bokmans. All rights reserved.
//

import UIKit

protocol WeatherViewControllerDatabaseDelegate : class {
    func addWeatherObject();
    func clearDatabase();
}

class EditWeatherViewController : UIViewController {
    
    weak var databaseDelegate : WeatherViewControllerDatabaseDelegate!
    
    @IBAction func addWeather(sender: AnyObject) {
        databaseDelegate.addWeatherObject()
        
    }
    @IBAction func clearDatabase(sender: AnyObject) {
        databaseDelegate.clearDatabase()
    }
}
