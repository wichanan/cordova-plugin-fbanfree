//
//  FBANFree.swift
//  
//
//  Created by Wichanan on 3/25/19.
//

@objc(FBANFree) class FBANFree: CDVPlugin {
    func echo(command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(
            status: CDVCommandStatus_ERROR
        )
        
        let msg = command.arguments[0] as? String ?? ""
        
        if !msg.isEmpty {
            /* UIAlertController is iOS 8 or newer only. */
            let toastController: UIAlertController =
                UIAlertController(
                    title: "",
                    message: msg,
                    preferredStyle: .alert
            )
            
            self.viewController?.present(
                toastController,
                animated: true,
                completion: nil
            )
            
            DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(3000)) {
                toastController.dismiss(
                    animated: true,
                    completion: nil
                )
            }
            
            pluginResult = CDVPluginResult(
                status: CDVCommandStatus_OK,
                messageAs: msg
            )
        }
        self.commandDelegate!.send(
            pluginResult,
            callbackId: command.callbackId
        )
    }
}
