package routes

import (
	"kalasetu/handlers"

	"github.com/gin-gonic/gin"
)

func RegisterUserRoutes(router *gin.RouterGroup, userHandler *handlers.UserHandler, authMiddleware gin.HandlerFunc) {
	userGroup := router.Group("/users")
	userGroup.Use(authMiddleware)
	{
		userGroup.PUT("/onboarding", userHandler.Onboarding)
	}
}
