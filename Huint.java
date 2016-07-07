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

public class Huint {// huint for "huge unsigned int"

 private int val[];// an array representing the huge unsigned integer
 private int top;// the top of the val array (since I push to it like a stack
 private int size;// the size of the val array

 public Huint() {
  val = new int[8];// the initial size of the array is arbitrarily 8,
       // since it is small and a power of 2
  size = 8;// the array size is initialized to 8
  top = -1;// the top of the "stack" is set to -1
  for (int i = 0; i < size; i++)
   val[i] = 0;
 }

 public Huint(String s) {// string constructor for Huint
  char c[] = s.toCharArray();
  size = c.length;// the array size is initialized to the array size
  val = new int[size];// the initial size of the array is arbitrarily 8,
       // since it is small and a power of 2
  top = size - 1;// the top of the "stack" is set to -1
  for (int i = 0; i < size; i++) {
   if (c[size - (i + 1)] == '0')
    val[i] = 0;
   else if (c[size - (i + 1)] == '1')
    val[i] = 1;
   else if (c[size - (i + 1)] == '2')
    val[i] = 2;
   else if (c[size - (i + 1)] == '3')
    val[i] = 3;
   else if (c[size - (i + 1)] == '4')
    val[i] = 4;
   else if (c[size - (i + 1)] == '5')
    val[i] = 5;
   else if (c[size - (i + 1)] == '6')
    val[i] = 6;
   else if (c[size - (i + 1)] == '7')
    val[i] = 7;
   else if (c[size - (i + 1)] == '8')
    val[i] = 8;
   else if (c[size - (i + 1)] == '9')
    val[i] = 9;
  }

 }

 public Huint(Huint a) {// This constructor make a copy of the existing Huint
  size = a.size;// the array size is initialized to 8
  val = new int[size];// the initial size of the array is arbitrarily 8,
       // since it is small and a power of 2
  top = a.top;// the top of the "stack" is set to -1

  for (int i = 0; i < size; i++)
   val[i] = a.val[i];
 }

 /* Constructor for the huint that takes an integer parameter */
 public Huint(int x) {
  val = new int[16];// the initial size of the array is 16, since it can
       // store more digits than the max int value
  size = 16;// the array size is initialized to 16
  top = -1;// the top of the "stack" is set to -1
  int i;// loop variable

  for (i = 0; i < size; i++) {// Here I fill up the entire val array with
         // values from x, the extra spaces filled
         // with zeros
   // val[i] = x%10;
   push(x % 10);// I take mod 10 of x to take away the least
       // signigicant number, then push that single digit
       // number onto the val array
   x = x / 10;// I divide by ten to take away that least sig digit
  }
  // printvalue();
 }

 /* This function returns the least sig number in a Huint */
 public int testnum() {
  if (top >= 0)
   return val[0];
  return 0;
 }
 
 
	/* This function returns the number of digits in a Huint */
	public int getlen() {
		int i;
		for (i = (size - 1); i >= 0; i--) {// I calculate the number of
											// meaningless leading zeros
			if (val[i] != 0)

				break;
		}
		i++;// i++ is the number of digits
		return i;
	}

 /*
  * This is the print value function. I prints out the value stored in a
  * Huint
  */
 public void printvalue() {
  int i;
  for (i = (size - 1); i >= 0; i--)
   System.out.print("" + val[i]);// loop though array, print out all
           // values
  System.out.println("");
 }

 /*
  * The addition function implements addition for the huint class, taking the
  * parameters of two huints
  */
 public void add(Huint a) {

  Huint result = new Huint();// A huint to store the answer
  int tmp = 0;// a tmp to store the value after addition
  int i;// loop variable
  Huint larger = new Huint(this);
  Huint smaller = new Huint(a);
  if (compare(a) == -1) {
   larger = new Huint(a);
   smaller = new Huint(this);
  }

  for (i = 0; (i < larger.size) && (i < smaller.size); i++) {// I loop
                 // though
                 // the
                 // entire
                 // val array
                 // size
   if (i < result.size)
    tmp = smaller.val[i] + larger.val[i] + result.val[i];// I add
                  // the
                  // current
                  // two
                  // digits
                  // being
                  // processed
   else
    tmp = smaller.val[i] + larger.val[i];
   if (tmp > 9) {// If I find the sum is 10 or more
    result.push(tmp % 10);// I push the lower digit onto the result
    result.push(1);// then add a one for the higher digit
    result.top = result.top - 1;// but since I may add more to that
           // one later, I decrement the top
           // variable to I can access the same
           // index again
   } else
    result.push(tmp);// if tmp is a single digit, I just add it to
         // my result
  }
  if (i != (larger.size)) {// this loop executes if the size of the second
         // array is less than the first, meaning
         // there are still digits to copy over
   for (; i < (result.size); i++) {// I loop until I reach the end of
           // the result array
    tmp = larger.val[i] + result.val[i];// I add the current two
             // digits being processed
    if (tmp > 9) {// If I find the sum is 10 or more
     result.push(tmp % 10);// I push the lower digit onto the
           // result
     result.push(1);// then add a one for the higher digit
     result.top = result.top - 1;// but since I may add more to
            // that one later, I decrement
            // the top variable to I can
            // access the same index again
    } else
     result.push(tmp);// if tmp is a single digit, I just add it
          // to my result
   }
   for (; i < larger.size; i++)// at this point, the result array has
          // reached its bounds as well, so I'm
          // only copying values from the first
          // array
    result.push(larger.val[i]);
  }

  // System.out.println("Addition: ");
  val = result.val;// I set the implied variable to the result
  top = result.top;
  size = result.size;
  // printvalue();
 }

 /*
  * This is the sub function, working to subrtact these huints from
  * eachother. Parameter a is always going to be subtracted from the implied
  * parameter. If parameter a is greater than the implied parameter, the
  * function returns false, showing that the operation was a failure
  */
 public boolean sub(Huint a) {

  Huint result = new Huint(this);// A huint to store the answer
  result.zeroOut();
  int tmp = 0;// a tmp to store the value after subtraction
  int i, j, k = 0;// loop variables

  for (i = 0; (i < size) && (i < a.size); i++) {// I loop though the
              // entire val array size
   j = i + 1;// I initialize j to be one greater than i
   if (val[i] < a.val[i]) {// if the current digit of the implied
         // parameter is less than what's being
         // subtracted
    if (j == size)// If I'm at the end of the array and val[i] <
        // a.val[i], the operation fails
     return false;
    while (val[j] == 0) {// I loop until I find a digit to the left
          // that is not zero
     k++;// tracking my progress by incrementing the k variable
     j++;
     if (j == size)// If I'm at the end of the array and val[i] <
         // a.val[i], the operation fails
      return false;
    }
    while (k != 0) {// This second loop moves back to the right and
        // maintains the digits accordingly
     val[j] = val[j] - 1;// the current j index value is
          // decremented, so that it may be
          // carried over
     j--;// j is decremented
     val[j] = val[j] + 10;// the new j index is increased by ten
     k--;// and k is decremented
    }
    val[j] = val[j] - 1;// at this point, I'm at the original j
         // index before the loop
    val[i] = val[i] + 10;// I take ten from the j index, and add it
          // to the i index so that I may finish
          // my subtraction operation
   }

   tmp = val[i] - a.val[i];// I sub the current two digits being
         // processed
   result.push(tmp);// and assign it to the proper location
  }

  // System.out.println("Subtraction: ");
  val = result.val;// I set the result value to the implied parameter
  top = result.top;
  size = result.size;
  // printvalue();
  return true;
 }

 /*
  * This is my compare function, comparing two huge unsigned ints. -1 is
  * returned if a is greater than the implied parameter 0 is returned if the
  * values are identical, and 1 is return if the implied parameter is greater
  * than the a parameter
  */
 public int compare(Huint a) {
  Huint tmp = new Huint(this);// I create a copy of the implied parameter
         // and store it in tmp (so the implied
         // parameter isn't overwritten when
         // subtracting)
  if (!(tmp.sub(a)))// I subtreact from the tmp huint, if the operation
       // failed
   return -1;// then that means that a is greater than tmp, and -1 is
      // returned

  int i = 0;// at this point, the subtraction was a sucess, and I must
     // check the result
  while ((i < size) && (tmp.val[i] == 0))// I navigate though each index
            // of the tmp array and make
            // sure it's zero
   i++;
  if (i == size)// If I reached the end of the array, the array was all
      // zeros, and the two parameters were the same value
   return 0;
  else
   return 1;// If I exited early, then I found a non-zero digit, which
      // means tmp was greater than the a parameter
 }

 /*
  * This is my compare function, comparing two huge unsigned ints. -1 is
  * returned if a is greater than the implied parameter 0 is returned if the
  * values are identical, and 1 is return if the implied parameter is greater
  * than the a parameter. This function is almost completely identical to the
  * one above, but takes an integer input
  */
 public int compare(int in) {
  Huint a = new Huint(in);// I make the integer input a Huint
  Huint tmp = new Huint(this);// I create a copy of the implied parameter
         // and store it in tmp (so the implied
         // parameter isn't overwritten when
         // subtracting)
  if (!(tmp.sub(a)))// I subtreact from the tmp huint, if the operation
       // failed
   return -1;// then that means that a is greater than tmp, and -1 is
      // returned

  int i = 0;// at this point, the subtraction was a sucess, and I must
     // check the result
  while ((i < size) && (tmp.val[i] == 0))// I navigate though each index
            // of the tmp array and make
            // sure it's zero
   i++;
  if (i == size)// If I reached the end of the array, the array was all
      // zeros, and the two parameters were the same value
   return 0;
  else
   return 1;// If I exited early, then I found a non-zero digit, which
      // means tmp was greater than the a parameter
 }

 /*
  * This is the multiplication function, its set up is similar to the set up
  * for addition, but it's implementation is similar to that of the
  * multiplication algorithm you learned in elementary school. It takes an
  * implied parameter and a Huint a, and changes the implied parameter to the
  * value of the result of the multiplication
  */
 public void mul(Huint a) {

  Huint result = new Huint();// A huint to store the answer
  int tmp = 0;// a tmp to store the value after addition
  int i, j, k;// loop variable
  Huint larger = new Huint(this);
  Huint smaller = new Huint(a);
  if (compare(a) == -1) {// I determine which value is larger, and save
        // the corresponding values into the variables
   larger = new Huint(a);
   smaller = new Huint(this);
  }

  for (i = 0; i < smaller.size; i++) {// I loop though the entire small
           // array size
   result.top = i - 1;// alternating the top of the array stack accouts
        // for zeros when multiplying by the second
        // digit of the smaller number
   for (j = 0; j <= larger.top; j++) {// I loop through the larger
            // array
    if ((i + j) < result.size)// I make sure I'm within range
     tmp = smaller.val[i] * larger.val[j] + result.val[j + i];// I
                    // add
                    // the
                    // current
                    // two
                    // digits
                    // being
                    // processed
                    // along
                    // with
                    // any
                    // value
                    // already
                    // in
                    // the
                    // result
                    // index
    else
     tmp = smaller.val[i] * larger.val[j];// if I'm out of bounds
               // of the result
               // array, no need to
               // add whatever may
               // be in result
               // index

    int tmptop = result.top;// I save the top of the array
    k = i + j + 1;// I move to next result index, storing value in k
        // looping variable
    while (tmp > 9) {// while my answer is two digits
     result.push(tmp % 10);// I push the lower digit onto the
           // result
     tmp = tmp / 10;// I take the higher digit(it's impossible
         // for this to be 3 digits if everything is
         // done right)
     if (k < result.size)// make sure I'm in the bounds of the
          // result array
      tmp += result.val[k];// and add the current tmp value
            // with the value store in the
            // result array
     k++;// then I increment k for further loops, if necessary
    }
    result.push(tmp);// if tmp is a single digit, I just add it to
         // my result
    result.top = tmptop + 1;// then I have to reset the top pointer
          // to what it should be without all the
          // carrying
   }
  }

  for (i = (result.size - 1); i >= 0; i--) {// I calculate the number of
             // meaningless leading zeros
   if (result.val[i] != 0) {
    result.top = i;
    break;
   }
  }
  Huint fin = new Huint();// I make a final return Huint
  for (i = 0; i <= result.top; i++)// I loop through all of the result
   fin.push(result.val[i]);// and push only the integers of value to
         // ignore the leading zeros
  // System.out.println("Addition: ");
  val = fin.val;// I set the implied variable to the result
  top = fin.top;
  size = fin.size;
  // printvalue();

 }

 /*
  * this is the modulous operation, returning void, but changing the value of
  * the implied parameter to the modulous of this%a, the implied parameter's
  * value modded with the parameter a
  */
 public void mod(Huint a) {
  Huint result = div(a);// the return from the divide function is the
        // remainder, I save that into a result Huint
  val = result.val;// I set the implied variable to the result
  top = result.top;
  size = result.size;
 }

 /*
  * This is the leftshift function, different purpose then the name implies.
  * It moves all integers stored in the Huint val array to the left by one
  * index, then adds the parameter x to the least significant location in the
  * array. This is relevent is certain operations
  */
 private void leftshift(int x) {
  if (top == -1) {// if Huint is empty, my action can be simulated by
      // simply pushing the x val onto the Huint
   push(x);
   return;
  }
  push(val[top]);// I push the value at the top of the stack onto the top
      // of the stack
  int i = top - 1;// I set my loop variable to one minus the top of the
      // stack
  while (i != 0) {// I move all values down one index
   val[i] = val[i - 1];
   i--;
  }
  val[i] = x;// at the lowest index, I set the val equal to x
 }

 /*
  * This is the zeroOut function. It zero outs and resets the implied
  * parameter's value array and top integer
  */
 private void zeroOut() {
  for (int i = 0; i < size; i++)// I loop though the entire array
   val[i] = 0;// set the value to zero
  top = -1; // I reset my top integer
 }

 /*
  * This is the division method, it takes the implied parameter and uses
  * integer division to divide by the parameter a, after the division is
  * complete, the answer is stored in the implied parameter
  */
 public Huint div(Huint a) {

  Huint copy1 = new Huint(this);// I copy the implied variable to avoid
          // altering it
  Huint result = new Huint(this);// I make a new huint to store my result
  Huint remainder = new Huint(this);// remainder from the operation
  result.zeroOut();// I made result copy the implied parameter so they
       // have the same size, but now I must zero out the
       // Huint
  remainder.zeroOut();

  for (int i = top; i >= 0; i--) {// I loop from the top of the Huint
   remainder.leftshift(0);// I leftshift
   result.top = result.top + 1;// I increment result's top
   remainder.val[0] = copy1.val[i];// I copy over the i index of the
           // copy to the least sig index in
           // remainder
   while (remainder.compare(a) != -1) {// while remainder is less than
            // a
    remainder.sub(a);// I sub a from remainder
    result.val[i] = result.val[i] + 1;// I increment result
   }
  }

  val = result.val;// I set the result value to the implied parameter
  top = result.top;
  size = result.size;
  return remainder;// I return the reminder from the division operation
 }

 /*
  * This method is a simple helper function testing whether or not a huint is
  * zero or not, returning a boolean true if it is zero, false otherwise.
  */
 public boolean isZero() {
  int i = 0;// at this point, the subtraction was a sucess, and I must
     // check the result
  while ((i < size) && (val[i] == 0))// I navigate though each index of
           // the tmp array and make sure it's
           // zero
   i++;
  if (i == size)// If I reached the end of the array, the array was all
      // zeros, and the two parameters were the same value
   return true;
  else
   return false;// If I exited early, then I found a non-zero digit,
       // which means tmp was greater than the a parameter
 }

 /*
  * My grow method is used to increase the size of the stack's int array once
  * it's full. It copies over all the values that were stored in the previous
  * array into a new, larger array, and the new array is returned
  */
 private void grow() {
  size = size * 2;
  int a[] = new int[size];// I alloc my new, larger array of ints
  int i;
  for (i = 0; i <= top; i++)// I loop through all values in the array
   a[i] = val[i];// and copy them over
  val = a;
 }

 /*
  * My push method takes a stack and an int, pushing the int onto the stack
  */
 public void push(int x) {
  if (top == (size - 1))// I check to see if the array is full
   grow();// if it is, I call the grow function and increase the
     // array's size
  top++;// I increment top
  // System.out.println("top: "+top + " Size: " + size);
  val[top] = x;// and add x the int stored at that index
 }

 public int getSize() {
  return size;
 }

 public int[] getVal() {
  return val;
 }

}
