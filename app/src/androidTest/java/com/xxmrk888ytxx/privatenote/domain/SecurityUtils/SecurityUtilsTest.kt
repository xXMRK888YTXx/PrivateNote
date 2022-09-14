package com.xxmrk888ytxx.privatenote.domain.SecurityUtils

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xxmrk888ytxx.privatenote.Utils.Exception.PasswordIsEmptyException
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import javax.crypto.BadPaddingException
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class SecurityUtilsTest {
    private val securityUtils:SecurityUtils = SecurityUtilsImpl()

    @Test
    fun checkPasswordToHashExpectPasswordHash() {
        val passwordList = listOf("test1463r","t","sdfhujiadfsjhoubfsjiajdohfjijshiroehiuoewrothe" +
                "euiuhiaejiroutheujirugihifsduifsdjfgdfuhdgdg")

        val hashList = passwordList.map { securityUtils.passwordToHash(it) }


        Assert.assertNotEquals(passwordList,hashList)
    }

    @Test(expected = PasswordIsEmptyException::class)
    fun ifSendEmptyStringExpectException() {
        val hash = securityUtils.passwordToHash("")
    }

    @Test
    fun testHashUncle() {
        val str1 = "Mozart"
        val str2 = "X simfonia"

        val hash1 = securityUtils.passwordToHash(str1)
        val hash2 = securityUtils.passwordToHash(str2)

        Assert.assertNotEquals(hash1,hash2)
    }

    @Test
    fun testHashSizeLimit() {
        val passwordList = mutableListOf("test1463r","t","sdfhujiadfsjhoubfsjiajdohfjijshiroehiuoewrothe" +
                "euiuhiaejiroutheujirugihifsduifsdjfgdfuhdgdg")


        passwordList[0] = securityUtils.passwordToHash(passwordList[0],8)
        passwordList[1] = securityUtils.passwordToHash(passwordList[1],32)
        passwordList[2] = securityUtils.passwordToHash(passwordList[2],64)

        if(passwordList[0].length != 8||passwordList[1].length != 32||passwordList[2].length != 64) {
            Assert.fail()
        }
    }

    @Test
    fun testHashWithoutSizeLimit() {
        var passwordList = listOf("test1463r","t","sdfhujiadfsjhoubfsjiajdohfjijshiroehiuoewrothe" +
                "euiuhiaejiroutheujirugihifsduifsdjfgdfuhdgdg")

        passwordList = passwordList.map { securityUtils.passwordToHash(it,0) }
        println(passwordList)
        passwordList.forEach {
            if(it.length != 64) {
                Assert.fail()
            }
        }
    }

    @Test
    fun testEncryptIfStringsNotEqualsAndPasswordNotEqualsMethodExpectEncryptNotEqualsStrings() {
        val str1 = "test"
        val str2 = "test2"
        val pas = getTestPasswordHash()
        val pas2 = getTestPasswordHash("testPassword2")

        val eStr = securityUtils.encrypt(str1,pas)
        val eStr2 = securityUtils.encrypt(str2,pas2)

        Assert.assertNotEquals(eStr,eStr2)
    }

    @Test
    fun testEncryptIfStringsEqualsAndPasswordNotEqualsMethodExpectEncryptNotEqualsStrings() {
        val str1 = "test"
        val str2 = "test"
        val pas = getTestPasswordHash()
        val pas2 = getTestPasswordHash("testPassword2")

        val eStr = securityUtils.encrypt(str1,pas)
        val eStr2 = securityUtils.encrypt(str2,pas2)

        Assert.assertNotEquals(eStr,eStr2)
    }

    @Test
    fun testEncryptIfStringsNotEqualsAndPasswordEqualsMethodExpectEncryptNotEqualsStrings() {
        val str1 = "test"
        val str2 = "test2"
        val pas = getTestPasswordHash()
        val pas2 = getTestPasswordHash()

        val eStr = securityUtils.encrypt(str1,pas)
        val eStr2 = securityUtils.encrypt(str2,pas2)

        Assert.assertNotEquals(eStr,eStr2)
    }

    @Test
    fun testEncryptIfStringsEqualsAndPasswordEqualsMethodExpectEncryptEqualsStrings() {
        val str1 = "test"
        val str2 = "test"
        val pas = getTestPasswordHash()
        val pas2 = getTestPasswordHash()

        val eStr = securityUtils.encrypt(str1,pas)
        val eStr2 = securityUtils.encrypt(str2,pas2)

        Assert.assertEquals(eStr,eStr2)
    }

    @Test(expected = PasswordIsEmptyException::class)
    fun testEncryptIfStringEmptyExpectException() {
        securityUtils.encrypt("giu","")
    }

    @Test
    fun testDecrepitImputeStringExpectPrimaryString() {
        val str = "Mozart X"
        val password = getTestPasswordHash()

        val eStr = securityUtils.encrypt(str,password)
        val dStr = securityUtils.decrypt(eStr,password)

        Assert.assertEquals(str,dStr)
    }

    @Test(expected = BadPaddingException::class)
    fun testDecrepitImputeStringIfPasswordIValidExpectException() {
        val str = "Mozart X"
        val password = getTestPasswordHash()
        val password2 = getTestPasswordHash("testPassword1")

        val eStr = securityUtils.encrypt(str,password)
        val dStr = securityUtils.decrypt(eStr,password2)
    }

    @Test(expected = PasswordIsEmptyException::class)
    fun testDecrepitIfPasswordEmptyExceptException() {
        securityUtils.decrypt("test","")
    }

    @Test
    fun testDecrepitIfStringEmptyExceptEmptyString() {
        val password = getTestPasswordHash()

        val str = securityUtils.decrypt("",password)

        Assert.assertEquals("",str)
    }

    @Test
    fun testDecrepitIfPasswordVeryLongExpectDecryptString() {
        val str = "test"
        val pass = getTestPasswordHash(Random(System.currentTimeMillis()).nextBytes(1000).toString())

        val eString = securityUtils.encrypt(str,pass)
        val dString = securityUtils.decrypt(eString,pass)

        Assert.assertEquals(str,dString)
    }

    @Test
    fun testDecrepitIfTextVeryLongExpectDecryptString() {
        val str =  Random(System.currentTimeMillis()).nextBytes(1000).toString()
        val pass = getTestPasswordHash()

        val eString = securityUtils.encrypt(str,pass)
        val dString = securityUtils.decrypt(eString,pass)

        Assert.assertEquals(str,dString)
    }

    private fun getTestPasswordHash(password:String = "testPassword") = securityUtils.passwordToHash(password)

}