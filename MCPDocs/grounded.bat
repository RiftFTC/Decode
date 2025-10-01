@echo off
REM Set environment variables
set OPENAI_API_KEY=lmstudio
set OPENAI_API_BASE=http://localhost:1234/v1

REM Run the docs-mcp-server with the specified embedding model
npx @arabold/docs-mcp-server@latest --embedding-model openai:text-embedding-qwen3-embedding-0.6b

pause
