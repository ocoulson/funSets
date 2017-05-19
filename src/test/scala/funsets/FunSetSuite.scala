package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)

    val zeroSet = singletonSet(0)

    val emptySet: Set = x => false
    val allIntegers: Set = x => true
    val allPositiveIntegers: Set = _ > 0
    val allEvenIntegers: Set = _ % 2 == 0
    val allOddIntegers: Set = _ % 2 != 0
    val allNegativeIntegers: Set = _ < 0

    val allPositiveEvenIntegers = intersect(allEvenIntegers, allPositiveIntegers)

    val allIntegersGreaterThan10: Set = _ > 10
  }

  /**
    * This test is currently disabled (by using "ignore") because the method
    * "singletonSet" is not yet implemented and the test would fail.
    *
    * Once you finish your implementation of "singletonSet", exchange the
    * function "ignore" by "test".
    */
  test("singletonSet(1) contains 1") {

    /**
      * We create a new instance of the "TestSets" trait, this gives us access
      * to the values "s1" to "s3".
      */
    new TestSets {
      /**
        * The string argument of "assert" is a message that is printed in case
        * the test fails. This helps identifying which assertion failed.
        */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains only elements in both sets") {
    new TestSets {
      val i = intersect(allEvenIntegers, allPositiveIntegers)
      assert(contains(i, 2))
      assert(contains(i, 4))
      assert(contains(i, 200))
      assert(contains(i, 12321232))

      assert(!contains(i, 1))
      assert(!contains(i, 45))
      assert(!contains(i, 125))

      assert(!contains(i, -2))
      assert(!contains(i, -40))
      assert(!contains(i, -1))
      assert(!contains(i, -5))
      assert(!contains(i, -101))
    }
  }

  test("difference contains only those elements of the first set which are not in the second") {
    new TestSets {
      val d = diff(allPositiveEvenIntegers, allIntegersGreaterThan10)

      assert(contains(d,2))
      assert(contains(d,4))
      assert(contains(d,6))
      assert(contains(d,8))
      assert(contains(d, 10))

      assert(!contains(d, 12))
      assert(!contains(d, 14))
      assert(!contains(d, 16))

      assert(!contains(d, 1))
      assert(!contains(d, 5))
      assert(!contains(d, 55))

      assert(!contains(d, -1))
      assert(!contains(d, -2))
      assert(!contains(d, -10))
    }
  }

  test("filter only contains elements in the set which are accepted by the predicate") {
    new TestSets {
      val f = filter(allPositiveIntegers, _ < 5)

      assert(contains(f, 1))
      assert(contains(f, 2))
      assert(contains(f, 3))
      assert(contains(f, 4))

      assert(!contains(f, 0))
      assert(!contains(f, 5))
      assert(!contains(f, 6))
      assert(!contains(f, 20))
      assert(!contains(f, 100))

      assert(!contains(f, -1))
      assert(!contains(f, -50))
      assert(!contains(f, -2))
      assert(!contains(f, -3))
      assert(!contains(f, -4))

    }
  }

  test("Forall determines whether all elements of a set conform to a predicate") {
    new TestSets {
      assert(forall(allPositiveIntegers, _ > 0))

      assert(!forall(allPositiveIntegers, x => x - 900 < 0))

      assert(forall(allIntegers, x => x - x == 0))

      assert(!forall(allNegativeIntegers, x => x % 501 != 0))

    }
  }

  test("Exists returns true if at least 1 bounded integer in the given set conforms to the predicate") {
    new TestSets {
      assert(exists(allIntegers, _ == 10))
      assert(exists(allIntegers, _ > 900))

      assert(!exists(allNegativeIntegers, _ == 10))
      assert(!exists(allIntegers, x => x < x - 5))

    }
  }

  test("Map") {
    new TestSets {

      printSet(s1)
      printSet(map(s1, x => 1))

      printSet(allPositiveEvenIntegers)
      printSet(map(allPositiveIntegers, _ + 1))
    }
  }

}
