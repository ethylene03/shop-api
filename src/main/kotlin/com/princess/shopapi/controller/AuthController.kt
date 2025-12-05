package com.princess.shopapi.controller

import com.princess.shopapi.dto.CredentialsDTO
import com.princess.shopapi.dto.UserDTO
import com.princess.shopapi.dto.UserTokenDTO
import com.princess.shopapi.service.AuthService
import com.princess.shopapi.service.UserService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/auth")
class AuthController(private val service: AuthService, private val userService: UserService) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/login")
    fun login(
        @RequestBody request: CredentialsDTO,
        response: HttpServletResponse
    ): UserTokenDTO {
        log.info("Running POST /login method.")
        return service.login(request, response)
            .also { log.info("User logged in.") }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signup(
        @Valid @RequestBody request: UserDTO
    ): UserDTO {
        log.info("Running POST /signup method.")
        return userService.create(request)
            .also { log.info("User signed up.") }
    }

    @PostMapping("/refresh")
    fun refreshToken(@CookieValue("refresh_token") token: String): UserTokenDTO {
        log.info("Running POST /auth/refresh method.")
        return service.refreshToken(token)
            .also { log.info("Access token refreshed.") }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun clearToken(response: HttpServletResponse) {
        log.info("Running DELETE /auth method.")
        service.clearToken(response)
            .also { log.info("User token cleared.") }
    }
}