package services

import (
	"context"
	"kalasetu/models"
	"kalasetu/repos"
)

type UserService interface {
	StartOnboarding(ctx context.Context, userID int, onboardingUser models.OnboardingUser) error
}

type userService struct {
	userRepo repos.UserRepository
}

func NewUserService(userRepo repos.UserRepository) UserService {
	return &userService{
		userRepo: userRepo,
	}
}

func (s *userService) StartOnboarding(ctx context.Context, userID int, onboardingUser models.OnboardingUser) error {
	return s.userRepo.StartOnboarding(ctx, userID, onboardingUser)
}
