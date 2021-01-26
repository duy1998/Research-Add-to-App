import UIKit
import Flutter

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Make a button to call the showFlutter function when pressed.
        let button = UIButton(type:UIButton.ButtonType.custom)
        button.addTarget(self, action: #selector(showFlutterWithoutCache), for: .touchUpInside)
        button.setTitle("Show Flutter without Cache", for: UIControl.State.normal)
        button.frame = CGRect(x: 80.0, y: 210.0, width: 250, height: 40.0)
        button.backgroundColor = UIColor.gray
        self.view.addSubview(button)
        // Make a button to call the showFlutter function when pressed.
        let button1 = UIButton(type:UIButton.ButtonType.custom)
        button1.addTarget(self, action: #selector(showFlutterWithCache), for: .touchUpInside)
        button1.setTitle("Show Flutter with Cache", for: UIControl.State.normal)
        button1.frame = CGRect(x: 80.0, y: 310, width: 250, height: 40.0)
        button1.backgroundColor = UIColor.gray
        self.view.addSubview(button1)
    }
    
    @objc func showFlutterWithoutCache() {
        let flutterViewController = FlutterViewController(project: nil, nibName: nil, bundle: nil)
        present(flutterViewController, animated: true, completion: nil)
    }
    
    @objc func showFlutterWithCache() {
        let flutterEngine = (UIApplication.shared.delegate as! AppDelegate).flutterEngine
        let flutterViewController =
            FlutterViewController(engine: flutterEngine, nibName: nil, bundle: nil)
        present(flutterViewController, animated: true, completion: nil)
    }
}
