package com.xxmrk888ytxx.privatenote.domain.SecurityUtils

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

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

    @Test
    fun ifSendEmptyStringExpectEmptyString() {
        val hash = securityUtils.passwordToHash("")

        Assert.assertEquals(hash,"")
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

//    @Test
//    fun encryptTestExpectEncryptStrings() {
//        //val strings = listOf<String>("test1,test2,test3")
//    }

}