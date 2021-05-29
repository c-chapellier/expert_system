# this is a comment
# all the required rules and symbols, along with the bonus ones, will be
# shown here. spacing is not important

(A | B) ^ C  =>  E  # A and B implies E : MAP OK
D  =>  E  # A and B implies E : MAP OK
# C           =>      E           # C implies E
# A + B + C   =>      D           # A and B and C implies D
#  A | B       =>      C           # A or B implies C
# A + !B      =>      F           # A and not B implies F
# C | !G      =>      H           # C or not G implies H
# V ^ W       =>      X           # V xor W implies X
# A + B       =>      Y + Z       # A and B implies Y and Z
# C | D       =>      X | V       # C or D implies X or V
# E + F       =>      !V          # E and F implies not V
# A + B       <=>     C           # A and B if and only if C
# A + B       <=>     !C          # A and B if and only if not C

=AB                        # Initial facts : A, B and G are true. All others are false.
                            # If no facts are initially true, then a simple "=" followed
                            # by a newline is used

?E                       # Queries : What are G, V and X ?
