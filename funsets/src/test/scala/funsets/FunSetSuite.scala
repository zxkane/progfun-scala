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
	test("string take") {
		val message = "hello, world"
				assert(message.take(5) == "hello")
	}

	/**
	 * For ScalaTest tests, there exists a special equality operator "===" that
	 * can be used inside "assert". If the assertion fails, the two values will
	 * be printed in the error message. Otherwise, when using "==", the test
	 * error message will only say "assertion failed", without showing the values.
	 *
	 * Try it out! Change the values so that the assertion fails, and look at the
	 * error message.
	 */
	test("adding ints") {
		assert(1 + 2 === 3)
	}


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

	test("union contains all elements") {
		new TestSets {
			val s = union(s1, s2)
					assert(contains(s, 1), "Union 1")
					assert(contains(s, 2), "Union 2")
					assert(!contains(s, 3), "Union 3")
		}
	}

	test("intersect elements") {
		new TestSets{
			val i1 = intersect(s1, s2)
					assert(!contains(i1, 1), "Intersect 1")
					val i2 = intersect(union(s1, s3), s3)
					assert(contains(i2, 3), "Intersect 2")
					assert(!contains(i2, 1), "Intersect 2.1")
		}
	}

	test("diff elements") {
		new TestSets{
			val i1 = diff(s1, s2)
					assert(contains(i1, 1), "Diff 1")
					val i2 = diff(union(s1, s2), s2)
					assert(contains(i2, 1), "Diff 2")
					assert(!contains(i2, 2), "Diff 2.1")
		}
	}

	test("filter elements") {
		new TestSets{
			val i1 = filter(s1, s2)
					assert(!contains(i1, 1), "filter 1")
					assert(!contains(i1, 2), "filter 1.2")
					val i2 = filter(union(s1, s2), s2)
					assert(!contains(i2, 1), "filter 2")
					assert(contains(i2, 2), "filter 2.1")
		}
	}  

	test("forall elements") {
		new TestSets{
			assert(!forall(s1, s2), "forall 1")
			assert(forall(s1, union(s1, s2)), "forall 2")
			assert(forall(union(s1, singletonSet(1000)), union(union(s1, s2), singletonSet(1000))), "forall 3")
			assert(forall(union(s1, singletonSet(-1000)), union(union(s1, singletonSet(-1000)), singletonSet(1000))), "forall 4")
			assert(forall(union(s1, singletonSet(1001)), s1), "forall 5")
			assert(forall(union(s1, singletonSet(-1001)), s1), "forall 6")
			assert(forall(s1, s1), "forall 7")
		}
	} 

	test("exists elements") {
		new TestSets{
			assert(!exists(s1, s2), "exists 1")
			assert(exists(s1, union(s1, s2)), "exists 2")
			assert(exists(union(s1, singletonSet(1000)), union(union(s1, s2), singletonSet(1000))), "exists 3")
			assert(exists(union(s1, singletonSet(-1000)), union(union(s1, singletonSet(-1000)), singletonSet(1000))), "exists 4")
			assert(exists(union(s1, singletonSet(1001)), s1), "exists 5")
			assert(!exists(union(s3, singletonSet(-1001)), s1), "exists 6")
			assert(exists(s1, s1), "exists 7")
		}
	} 

	test("map elements") {
		new TestSets{
			def f = {(x: Int) => x * x}

			var s = map(s1, f)
					assert(contains(s, 1), "map 1")
					assert(!contains(s, 2), "map 2")
					s = map(union(s2, s3), f)
					assert(!contains(s, 1), "map 3")
					assert(!contains(s, 2), "map 4")
					assert(!contains(s, 3), "map 5")
					assert(forall(s, union(singletonSet(4), singletonSet(9))), "map 6")
		}
	} 	
}
