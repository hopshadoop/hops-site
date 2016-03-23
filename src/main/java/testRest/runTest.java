/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testRest;

/**
 *
 * @author archer
 */
public class runTest {
    
    
    public static void main(String [] args){
        
        NewJerseyClient c = new NewJerseyClient();
        
        String s = c.getIt();
        
        System.out.println(s);
        
    }
    
}
