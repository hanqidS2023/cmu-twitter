# Stage 1: Build the React application
FROM node:14 as build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . ./
RUN npm run build

# Stage 2: Serve the application with Express
FROM node:14

WORKDIR /app

# Copy the build from the previous stage
COPY --from=build /app/build ./build

# Copy server.js
COPY server.js ./

# Install Express
RUN npm install express

EXPOSE 80

CMD ["node", "server.js"]
