/**
 * ************************************************************************************************
 * Antonio Wayne Shelton - ashelt7@uic.edu 
 * Borislav Strahilov - bstrah2@uic.edu
 *
 * CS342 Project 03 - A java-based implementation of the RSA encryption
 * algorithm
 * 
 * files Version 1.0
 *
 *************************************************************************************************
 */

import java.awt.*;
import java.lang.*;
import javax.swing.*;


public class RSA {
  
  /*This function determines whether or not an inputted Huint is a prime number or not.
   * This function is based off of the example written in python found on stackoverflow
   * at http://stackoverflow.com/questions/1801391/what-is-the-best-algorithm-for-checking-if-a-number-is-prime
   * and we do not claim to have come up with it ourselves*/
  public static boolean testPrime(Huint a) {
    Huint tmp = new Huint( a );//I copy over the huint into a tmp huint so I don't distort the value
    Huint test = new Huint( 2 );//a constant two for comparison, since comparison can only be done between Huints
    if( tmp.compare(test) == 0 )//If the Huint a is two
      return true;//this number is prime
    tmp.mod(test);//I mod a with 2
    test = new Huint();//I set test to zero
    if( tmp.compare(test) == 0 )//if the number modded with two is zero, this number is not prime
      return false;
    
    
    tmp = new Huint( a );//I set tmp to a again
    test = new Huint( 3 );//test to 3
    if( tmp.compare(test) == 0 )//If tmp is 3
      return true;//the number is prime
    tmp.mod(test);//I mod with 3
    test = new Huint();//set test to zero
    if( tmp.compare(test) == 0 )//if modded result was zero
      return false;//number ain't prime
    
    int i = 5;//set variables i to 5, w to 2
    int w = 2;
    while( a.compare(i*i) != -1 ) { //while a is greater than or equal to i squared
      tmp = new Huint(a);//I copy a
      test = new Huint(i);//I make my test i
      tmp.mod(test);//I mod a and i
      if( tmp.isZero() )//if the result is zero
        return false;//I found a value divisable by the number being tested, number not prime
      i += w;//i plus w
      w = 6 - w;//6-w
    }
    return true;//if I make it this far, the number is prime
  }
  
  /*This is the gcd function, or the greatest common denominator function. It is based on
   * the Euclidean algorithm, and the basis for this code was found on stackoverflow at
   * http://stackoverflow.com/questions/12826114/euclids-extended-algorithm-c
   * We do not claim to have come up with this algorithm ourselves.
   * This function takes two parameters a and b, and returns the greatest common denominator
   * between the two, also in the form of a Huge integer*/
  public static Huint gcd( Huint a, Huint b) {
    if( b.isZero() ) {//I test if b is zero
      return a;//If it is, I return a
    }
    Huint atmp = new Huint(a);
    atmp.mod(b);//If not, I mod a by b
    return gcd( b, atmp );//and make a recursive call, with the parameters flipped and return the result of the recursive call
  }
  
  
  /*This is the key creation function. It takes in two prime number in the form of huint x and huint y and
   * generates values e and d (encode and decode) according to the RSA algorithm's rules. The values are stored
   * into an XML file*/
  public static void keyCreation( Huint x, Huint y ) {
    
    if( (!testPrime(x)) || (!testPrime(y)) ) {//if either of the inputted values are not prime, I exit without doing anything
      JOptionPane.showConfirmDialog(null,"At least one of the values entered is not a prime number!",null, JOptionPane.PLAIN_MESSAGE);
      return;
    }
    
    Huint n = new Huint(x);//I calculate the n value during the RSA process
    n.mul(y);//just the two primes multiplied
    
    Huint constant = new Huint( 1 );//I save a constant value of one into a Huint
    x.sub(constant);//I decrement x
    y.sub(constant);//I decrement y
    Huint phi = new Huint( x );//I calculate phi, (x-1)(y-1)
    phi.mul(y);
    
    Huint e = new Huint(2);//I initialize e to two, since 1 is always a valid e value
    int hitCounter = 0;//I have a hit counter to count the number of times I find a valid value for e (or d, later)
    Huint gcdtmp;//I make a tmp to store the return from the gcd function
    while( e.compare(n) == -1 ) {//while e is less than n
      gcdtmp = gcd( e, phi );//I calcuate the gcd of e and phi
      if( gcdtmp.compare( constant ) == 0 ) {//If I find it is one
        hitCounter++;//This value is valid, and I increment my hit counter
        if( hitCounter == 8 )//If this is the eigth valid value, I make it my e value and
          break;//break from the loop
      }
      e.add(constant);//I increment e my one
    }
    
    if( e.compare(n) != -1 ) {//If I make it this far and e is equal to n
      System.out.println("The value of e reached n before a valid e value could be calculated");//Something broke that shouldn't have
      return;
      }
    
    Huint d;//I initialize my d huint
    Huint modtmp;//I make a Huint to store the result from a mod operation
    Huint i = new Huint(1);//I make a loop huint
    while( true ) {//forever loop
      modtmp = new Huint( phi );//I copy phi into modtmp
      modtmp.mul(i);//I multiply it by my loop value
      modtmp.add(constant);//I add one
      modtmp.mod(e);//I mod it by the e value
      if( modtmp.isZero() ) {//If I find there was no remainder
        hitCounter++;//I have a hit
          d = new Huint( phi );//I set d to be this value
          d.mul(i);
          d.add(constant);
          d.div(e);
          break;//and break the loop
      }
      i.add(constant);//I add one to my loop counter
    }
    
    FileHandler.createPrivateKey(d, n);
    FileHandler.createPublicKey(e, n);
    
   

  }
  
  /*This endecrypt function serves as both the encrypt function and the decrypt function,
  * since the operations performed are the same in both cases. The parameter ed is to be
  * either the encrypt Huint value e or the decrypt value d, depending on wether the user
  * intends to encrypt or decypt a value. The Huint n is the value n that is the product 
  * of the two original prinme numbers. The Huint value x is the number that consists of 
  * the ascii characters that make up the message*/
  
  
  
//  public static Huint endecrypt( Huint ed, Huint n, Huint x ) {
//    Huint cons = new Huint(1);//A constant value of one
//    Huint ret = new Huint(1);//My return huint, the final message after being encrypted or decrypted
//    Huint i = new Huint(0);//a loop variable
//    
//    for(; i.compare(ed) == -1; i.add(cons) ) {//while i < ed, add one to the loop variable
//      ret.mul(x);//I multiply the return by x
//      ret.mod(n);//I mod the return with n
//    }
//    return ret;//I return the final answer
//  }
  
 /*This endecrypt function serves as both the encrypt function and the decrypt function,
  * since the operations performed are the same in both cases. The parameter ed is to be
  * either the encrypt Huint value e or the decrypt value d, depending on wether the user
  * intends to encrypt or decypt a value. The Huint n is the value n that is the product 
  * of the two original prinme numbers. The Huint value x is the number that consists of 
  * the ascii characters that make up the message*/
  public static Huint endecrypt( Huint ed, Huint n, Huint x ) {
   /* Huint cons = new Huint(1);//A constant value of one
    Huint ret = new Huint(1);//My return huint, the final message after being encrypted or decrypted
    Huint i = new Huint(0);//a loop variable
    
    for(; i.compare(ed) == -1; i.add(cons) ) {//while i < ed, add one to the loop variable
      ret.mul(x);//I multiply the return by x
      ret.mod(n);//I mod the return with n
    }
    return ret;//I return the final answer*/
    
    Huint cons = new Huint(2);
    Huint ret = new Huint(1);//My return huint, the final message after being encrypted or decrypted
    Huint edclone = new Huint();//copies so parameters aren't altered
    Huint base = new Huint();
    base.add(x);
    edclone.add(ed);
    
    while( edclone.compare(0) == 1 ) {//while ed > 0, add one to the loop variable
      if( (edclone.testnum()%2) == 1 ){//if ed is odd
        ret.mul(base);//return is multiplied by the base
        ret.mod(n);//and modded with n
      }
      edclone.div(cons);//I divide by two
      base.mul(base);//I square the base
      base.mod(n);//I mod the base with n
     // edclone.printvalue();
      //ret.printvalue();
      //x.printvalue();
      //System.out.println("");
    }
    ret.mod(n);//I mod the return with n
    
    //ret.printvalue();
    return ret;
    
  }
  
}






















