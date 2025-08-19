# Enhanced Recommendation System with LLM Integration

## Overview

The TourSaaS recommendation system has been enhanced with LLM (Large Language Model) capabilities, providing intelligent and personalized travel recommendations using both local Ollama and cloud-based OpenAI services.

## Features

### 1. LLM-Powered Recommendations
- **Personalized recommendations** based on user preferences and travel history
- **Intelligent analysis** of user behavior and patterns
- **Customized travel suggestions** tailored to individual user profiles
- **Fallback mechanism** to traditional algorithms if LLM services are unavailable

### 2. Multiple LLM Providers
- **Ollama (Local)**: Run LLM models locally for privacy and cost efficiency
- **OpenAI (Cloud)**: Use GPT models for advanced reasoning capabilities

### 3. Smart Analysis
- **User behavior analysis**: Understand travel patterns and preferences
- **Personalized suggestions**: Generate custom travel itineraries
- **Budget and season considerations**: Factor in practical constraints

## Configuration

### Application Configuration (application.yml)

```yaml
llm:
  provider: ollama  # ollama, openai
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

### 1. LLM-Powered Recommendations
```
POST /api/recommendations/llm/generate/{userId}?provider={provider}
```
Generate intelligent recommendations using LLM analysis.

**Parameters:**
- `userId`: User ID for recommendations
- `provider`: LLM provider (ollama, openai)

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
Generate personalized travel suggestions and itineraries.

**Response:**
```json
"Based on your preferences for domestic travel and budget-conscious choices, I recommend visiting..."
```

### 3. User Behavior Analysis
```
POST /api/recommendations/llm/analyze/{userId}?provider={provider}
```
Analyze user behavior patterns and preferences.

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
Check availability of LLM services.

**Response:**
```json
{
  "ollama": true,
  "openai": false
}
```

### 5. Custom LLM Request
```
POST /api/recommendations/llm/custom
```

**Request Body:**
```json
{
  "userId": "1",
  "provider": "ollama",
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

1. Update `application.yml` with your preferred LLM provider
2. Set appropriate model names and API keys
3. Configure base URLs for local deployments

## Usage Examples

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

1. **LlmService Interface**: Common interface for LLM operations
2. **OllamaService**: Implementation for local Ollama integration
3. **OpenAIService**: Implementation for OpenAI API integration
4. **LlmServiceFactory**: Factory pattern for provider selection
5. **RecommendationService**: Enhanced with LLM capabilities
6. **RecommendationController**: New REST endpoints for LLM features

### Data Flow

1. User request → Controller → Service
2. Service builds user profile and product data
3. LLM service generates intelligent recommendations
4. Response parsed and returned to user
5. Fallback to traditional algorithms if LLM fails

## Security Considerations

- API keys are stored in environment variables
- Local Ollama provides data privacy
- Request validation and error handling
- Fallback mechanisms ensure service availability

## Performance

- Async processing with WebClient
- Configurable timeouts
- Connection pooling for HTTP clients
- Caching recommendations for better performance

## Troubleshooting

### Common Issues

1. **Ollama connection failed**: Ensure Ollama service is running
2. **OpenAI API errors**: Check API key and network connectivity
3. **Model not found**: Verify model name in configuration
4. **Timeout errors**: Increase timeout values in configuration

### Debug Mode

Enable debug logging:
```yaml
logging:
  level:
    com.toursaas.service.impl: DEBUG
    org.springframework.web.reactive: DEBUG
```

## Future Enhancements

- Support for additional LLM providers (Anthropic, Gemini)
- Advanced user segmentation and targeting
- Real-time recommendation updates
- Multi-language support
- Integration with external travel APIs

## 技术支持

如有技术问题，请联系：
- 项目地址: [https://github.com/EnZ0cez/Tour-Saas/](https://github.com/EnZ0cez/Tour-Saas/)
- 邮箱: enzochen617@gmail.com
- 问题反馈: [GitHub Issues](https://github.com/EnZ0cez/Tour-Saas/issues)