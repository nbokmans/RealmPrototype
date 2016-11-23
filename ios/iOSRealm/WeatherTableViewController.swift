//
//  WeatherTableViewController.swift
//  iOSRealm
//
//  Created by Niels Bokmans on 08/11/16.
//  Copyright © 2016 Niels Bokmans. All rights reserved.
//

import UIKit
import RealmSwift
class WeatherTableViewController : UITableViewController {
    var token: NotificationToken?
    
    let authURL: NSURL = NSURL(string: "http://185.14.186.238:9080")!
    let realmURL: NSURL = NSURL(string: "realm://185.14.186.238:9080/~/newRealm")!
    let username = ""
    let password = ""

    override func viewDidLoad() {
        super.viewDidLoad()
        try! synchronouslyLogInUser()
    }
    
    private func synchronouslyLogInUser() throws {
        SyncUser.authenticateWithCredential(Credential.usernamePassword(username, password: password, actions:.UseExistingAccount), authServerURL: authURL) { (user, error) in
            if let user = user {
                print("login successful")
                self.setDefaultRealmConfiguration(user)
            }
        }
    }
    
    private func setDefaultRealmConfiguration(user: SyncUser) {
        Realm.Configuration.defaultConfiguration = Realm.Configuration(syncConfiguration: (user, realmURL), objectTypes: [Weather.self, Wind.self])
        setTableViewChangeListener()
    }
    
    private func setTableViewChangeListener() {
        let realm = try! Realm()
        let results = realm.objects(Weather)
        print(results)
        token = results.addNotificationBlock { [weak self] (changes: RealmCollectionChange) in
            guard let tableView = self?.tableView else { return }
            switch changes {
            case .Initial:
                // Results are now populated and can be accessed without blocking the UI
                tableView.reloadData()
                break
            case .Update(_, let deletions, let insertions, let modifications):
                // Query results have changed, so apply them to the UITableView
                tableView.beginUpdates()
                tableView.insertRowsAtIndexPaths(insertions.map({ NSIndexPath(forRow: $0, inSection: 0) }),
                    withRowAnimation: .Automatic)
                tableView.deleteRowsAtIndexPaths(deletions.map({ NSIndexPath(forRow: $0, inSection: 0)}),
                    withRowAnimation: .Automatic)
                tableView.reloadRowsAtIndexPaths(modifications.map({ NSIndexPath(forRow: $0, inSection: 0) }),
                    withRowAnimation: .Automatic)
                tableView.endUpdates()
                break
            case .Error(let error):
                // An error occurred while opening the Realm file on the background worker thread
                print("\(error)")
                break
            }
        }
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cellIdentifier = "WeatherTableViewCell"
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier, forIndexPath: indexPath) as! WeatherTableViewCell
        let realm = try! Realm()
        
        cell.weather.text = realm.objects(Weather)[indexPath.row].description
        print(cell.weather.text)
        return cell
    }
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let realm = try! Realm()
        return realm.objects(Weather).count
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if let destinationVC = segue.destinationViewController as? EditWeatherViewController {
            destinationVC.databaseDelegate = self
        }
    }
    
    deinit {
        token?.stop()
    }
}

extension WeatherTableViewController : WeatherViewControllerDatabaseDelegate {
    func addWeatherObject() {
        let realm = try! Realm()
        try! realm.write {
            let weather = Weather()
            let wind = Wind()
            weather.date = "09-11-2016"
            weather.forecast = "Lekker zonnig"
            weather.humidity = "40%"
            wind.direction = "NE"
            wind.speed = "8"
            weather.wind = wind
            realm.add(weather, update: false)
        }
    }
    
    func clearDatabase() {
        let realm = try! Realm()
        try! realm.write {
            realm.deleteAll()
        }
    }
}
