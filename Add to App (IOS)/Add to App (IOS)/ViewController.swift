import UIKit
import Flutter
// Used to connect plugins (only if you have plugins with iOS platform code).
import FlutterPluginRegistrant

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Make a button to call the showFlutter function when pressed.
        let button = UIButton(type:UIButton.ButtonType.custom)
        button.addTarget(self, action: #selector(showFlutter), for: .touchUpInside)
        button.setTitle("Show Flutter", for: UIControl.State.normal)
        button.frame = CGRect(x: 60.0, y: 210.0, width: 300, height: 40.0)
        button.backgroundColor = UIColor.gray
        self.view.addSubview(button)
        
        let button1 = UIButton(type:UIButton.ButtonType.custom)
        button1.addTarget(self, action: #selector(showFlutterWithRoute), for: .touchUpInside)
        button1.setTitle("Show Flutter with Route", for: UIControl.State.normal)
        button1.frame = CGRect(x: 60.0, y: 310, width: 300, height: 40.0)
        button1.backgroundColor = UIColor.gray
        self.view.addSubview(button1)
        
        // Make a button to call the showFlutter function when pressed.
        let button2 = UIButton(type:UIButton.ButtonType.custom)
        button2.addTarget(self, action: #selector(showFlutterWithCache), for: .touchUpInside)
        button2.setTitle("Show Flutter with Cache", for: UIControl.State.normal)
        button2.frame = CGRect(x: 60.0, y: 410, width: 300, height: 40.0)
        button2.backgroundColor = UIColor.gray
        self.view.addSubview(button2)
        
        // Make a button to call the showFlutter function when pressed.
        let button3 = UIButton(type:UIButton.ButtonType.custom)
        button3.addTarget(self, action: #selector(showFlutterWithCacheRoute), for: .touchUpInside)
        button3.setTitle("Show Flutter with Cache, Route", for: UIControl.State.normal)
        button3.frame = CGRect(x: 60.0, y: 510, width: 300, height: 40.0)
        button3.backgroundColor = UIColor.gray
        self.view.addSubview(button3)
    }
    
    @objc func showFlutter() {
        let flutterViewController = FlutterViewController(project: nil, nibName: nil, bundle: nil)
        present(flutterViewController, animated: true, completion: nil)
    }
    
    @objc func showFlutterWithRoute() {
        let flutterEngine1 = FlutterEngine(name: "my flutter engine")
        flutterEngine1.run(withEntrypoint: nil,initialRoute: "/route_1")
        let flutterViewController =
            FlutterViewController(engine: flutterEngine1, nibName: nil, bundle: nil)
        present(flutterViewController, animated: true, completion: nil)
    }
    
    @objc func showFlutterWithCache() {
        let flutterEngine = (UIApplication.shared.delegate as! AppDelegate).flutterEngine
        let flutterViewController =
            FlutterViewController(engine: flutterEngine, nibName: nil, bundle: nil)
        present(flutterViewController, animated: true, completion: nil)
    }
    
    @objc func showFlutterWithCacheRoute() {
        let flutterEngine = (UIApplication.shared.delegate as! AppDelegate).flutterEngineRoute
        let flutterViewController =
            FlutterViewController(engine: flutterEngine, nibName: nil, bundle: nil)
        present(flutterViewController, animated: true, completion: nil)
    }
}
