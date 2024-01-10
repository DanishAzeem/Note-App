import java.util.regex.Matcher

class Util {
    companion object {
        private val PASSWORD_PATTERN =
            "^(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=.*\\S+$)" +
                ".{6,}$"

        private val PASSWORD_LOWERCASE_PATTERN = "(?=.*[a-z]).{0,}"
        private val PASSWORD_UPPERCASE_PATTERN = "(?=.*[A-Z]).{0,}"
        private val PASSWORD_NUMBER_PATTERN = "((?=.*[0-9]).{0,})"
        private val PASSWORD_SPECIAL_PATTERN = "((?=.*[@#\$%^&+=]).{0,})"



        fun isLowerCasePatternValid(password: String): Boolean {
            val matcher: Matcher = PASSWORD_LOWERCASE_PATTERN.toPattern().matcher(password)
            return matcher.matches()
        }
        fun isUpperCasePatternValid(password: String): Boolean {
            val matcher: Matcher = PASSWORD_UPPERCASE_PATTERN.toPattern().matcher(password)
            return matcher.matches()
        }
        fun isNumberPatternValid(password: String): Boolean {
            val matcher: Matcher = PASSWORD_NUMBER_PATTERN.toPattern().matcher(password)
            return matcher.matches()
        }
        fun isSpecialCharPatternValid(password: String): Boolean {
            val matcher: Matcher = PASSWORD_SPECIAL_PATTERN.toPattern().matcher(password)
            return matcher.matches()
        }
    }
}

