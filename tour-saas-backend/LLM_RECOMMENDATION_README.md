# Enhanced Recommendation System with Spring AI Integration

## Overview

The TourSaaS recommendation system has been enhanced with Spring AI framework, providing intelligent and personalized travel recommendations using a unified AI interface that supports multiple LLM providers including local Ollama and cloud-based OpenAI services.

## Features

### 1. Spring AI Integration
- **Unified Interface**: Use Spring AI's ChatClient for consistent AI interactions
- **Multiple Provider Support**: Seamlessly switch between different LLM providers
- **Automatic Configuration**: Spring Boot auto-configuration for AI services
- **Dependency Injection**: Easy integration with existing Spring applications

### 2. LLM-Powered Recommendations
- **Personalized recommendations** based on user preferences and travel history
- **Intelligent analysis** of user behavior and patterns
- **Customized travel suggestions** tailored to individual user profiles
- **Fallback mechanism** to traditional algorithms if LLM services are unavailable

### 3. Multiple LLM Providers
- **Ollama (Local)**: Run LLM models locally for privacy and cost efficiency
- **OpenAI (Cloud)**: Use GPT models for advanced reasoning capabilities
- **Spring AI**: Unified framework supporting both providers

### 4. Smart Analysis
- **User behavior analysis**: Understand travel patterns and preferences
- **Personalized suggestions**: Generate custom travel itineraries
- **Budget and season considerations**: Factor in practical constraints

## Configuration

### Application Configuration (application.yml)

```yaml
# Spring AI Configuration
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:}
      chat:
        options:
          model: gpt-3.5-turbo
          temperature: 0.7
          max-tokens: 1000
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: llama3.2
          temperature: 0.7
          max-tokens: 1000

# Backward Compatible LLM Configuration
llm:
  provider: spring  # spring, ollama, openai
  model: llama3.2    # default model name
  base-url: http://localhost:11434  # default base url
  api-key: ${OPENAI_API_KEY:}  # OpenAI API key
  max-tokens: 1000
  temperature: 0.7
  timeout: 30s
  
  # Provider specific settings
  openai-model: gpt-3.5-turbo
  ollama-model: llama3.2
  ollama-base-url: http://localhost:11434
  
  # Spring AI specific settings
  spring-model: llama3.2
  spring-provider: ollama
```

### Environment Variables

```bash
# For OpenAI integration
export OPENAI_API_KEY="your-openai-api-key"

# For Ollama (ensure Ollama is running)
# Start Ollama: ollama serve
# Pull model: ollama pull llama3.2
```

## API Endpoints

### 1. Spring AI-Powered Recommendations
```
POST /api/recommendations/llm/generate/{userId}?provider={provider}
```
Generate intelligent recommendations using Spring AI framework.

**Parameters:**
- `userId`: User ID for recommendations
- `provider`: AI provider (spring, ollama, openai)

**Response:**
```json
[
  {
    "id": 1,
    "user": {...},
    "product": {...},
    "score": 0.95,
    "createdAt": "2025-08-18T17:00:00"
  }
]
```

### 2. Personalized Travel Suggestions
```
POST /api/recommendations/llm/suggestions/{userId}?provider={provider}
```
Generate personalized travel suggestions and itineraries using Spring AI.

**Parameters:**
- `userId`: User ID for suggestions
- `provider`: AI provider (spring, ollama, openai)

**Response:**
```json
"Based on your preferences for domestic travel and budget-conscious choices, I recommend visiting..."
```

### 3. User Behavior Analysis
```
POST /api/recommendations/llm/analyze/{userId}?provider={provider}
```
Analyze user behavior patterns and preferences using Spring AI.

**Parameters:**
- `userId`: User ID for analysis
- `provider`: AI provider (spring, ollama, openai)

**Response:**
```json
{
  "preferences": {...},
  "patterns": {...},
  "suggestions": [...]
}
```

### 4. Service Status Check
```
GET /api/recommendations/llm/status
```
Check availability of AI services.

**Response:**
```json
{
  "spring": true,
  "ollama": true,
  "openai": false
}
```

### 5. Custom AI Request
```
POST /api/recommendations/llm/custom
```

**Request Body:**
```json
{
  "userId": "1",
  "provider": "spring",
  "userPreferences": "...",
  "userProfile": "...",
  "availableProducts": "..."
}
```

## Setup Instructions

### 1. Ollama Setup (Local LLM)

```bash
# Install Ollama
# Visit: https://ollama.ai/

# Start Ollama service
ollama serve

# Pull recommended model
ollama pull llama3.2

# Verify installation
ollama list
```

### 2. OpenAI Setup (Cloud LLM)

```bash
# Get API key from OpenAI dashboard
# Set environment variable
export OPENAI_API_KEY="sk-your-api-key-here"
```

### 3. Application Configuration

1. Update `application.yml` with your preferred AI provider
2. Configure Spring AI settings for OpenAI and Ollama
3. Set appropriate model names and API keys
4. Configure base URLs for local deployments
5. The system will automatically use Spring AI as the default provider

## Usage Examples

### Generate Recommendations with Spring AI
```bash
curl -X POST "http://localhost:8080/api/recommendations/llm/generate/1?provider=spring" \
  -H "Content-Type: application/json"
```

### Generate Recommendations with Ollama
```bash
curl -X POST "http://localhost:8080/api/recommendations/llm/generate/1?provider=ollama" \
  -H "Content-Type: application/json"
```

### Generate Suggestions with OpenAI
```bash
curl -X POST "http://localhost:8080/api/recommendations/llm/suggestions/1?provider=openai" \
  -H "Content-Type: application/json"
```

### Check Service Status
```bash
curl -X GET "http://localhost:8080/api/recommendations/llm/status"
```

## Architecture

### Components

1. **LlmService Interface**: Common interface for AI operations
2. **SpringAIService**: Spring AI framework implementation
3. **OllamaService**: Implementation for local Ollama integration
4. **OpenAIService**: Implementation for OpenAI API integration
5. **LlmServiceFactory**: Factory pattern for provider selection
6. **RecommendationService**: Enhanced with AI capabilities
7. **RecommendationController**: REST endpoints for AI features

### Data Flow

1. User request → Controller → Service
2. Service builds user profile and product data
3. Spring AI service generates intelligent recommendations
4. Response parsed and returned to user
5. Fallback to traditional algorithms if AI service fails

## Security Considerations

- API keys are stored in environment variables
- Local Ollama provides data privacy
- Request validation and error handling
- Fallback mechanisms ensure service availability

## Performance

- **Spring AI Optimization**: Built-in connection pooling and caching
- **Unified Interface**: Consistent performance across different AI providers
- **Automatic Configuration**: Optimized settings for different AI models
- **Error Handling**: Built-in retry mechanisms and fallback strategies
- **Dependency Injection**: Efficient resource management through Spring container

## Troubleshooting

### Common Issues

1. **Ollama connection failed**: Ensure Ollama service is running
2. **OpenAI API errors**: Check API key and network connectivity
3. **Model not found**: Verify model name in configuration
4. **Timeout errors**: Increase timeout values in configuration
5. **Spring AI configuration**: Check Spring AI settings in application.yml
6. **Dependency issues**: Ensure Spring AI dependencies are properly loaded

### Debug Mode

Enable debug logging:
```yaml
logging:
  level:
    com.toursaas.service.impl: DEBUG
    org.springframework.ai: DEBUG
    org.springframework.web.reactive: DEBUG
```

## Future Enhancements

- Support for additional AI providers through Spring AI (Anthropic, Gemini)
- Advanced user segmentation and targeting
- Real-time recommendation updates
- Multi-language support
- Integration with external travel APIs
- Enhanced Spring AI features and capabilities
- Improved caching and performance optimization

## 技术支持

如有技术问题，请联系：
- 项目地址: [https://github.com/EnZ0cez/Tour-Saas/](https://github.com/EnZ0cez/Tour-Saas/)
- 邮箱: enzochen617@gmail.com
- 问题反馈: [GitHub Issues](https://github.com/EnZ0cez/Tour-Saas/issues)