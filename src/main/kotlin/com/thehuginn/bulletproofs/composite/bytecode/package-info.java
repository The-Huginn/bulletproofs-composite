/**
 * This package serves as an internal mechanism designed to intercept and manage
 * interactions with the external libraries 'bulletproofs' and 'bulletproofs-gadget'.
 *
 * <p>
 * The primary purpose of this package is to provide a layer of abstraction that
 * handles the dynamic invocation of methods in these external libraries. This is
 * achieved through the use of Java reflection, allowing the system to create single
 * valid composite proof comprised of otherwise individual proofs without requiring
 * modifications to the core library logic.
 * </p>
 *
 * <p>
 * Key responsibilities of this package include is to interception method calls,
 * mainly commit and prove/verify
 * </p>
 *
 * <p>
 * Note: This package is intended solely for internal use. It is not part of the public
 * API and is subject to change without notice.
 * </p>
 */
package com.thehuginn.bulletproofs.composite.bytecode;