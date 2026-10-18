FROM node:22-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
ARG VITE_BACK_END_URL
ENV VITE_BACK_END_URL=$VITE_BACK_END_URL

RUN npm run build

FROM node:22-alpine
RUN npm install --global serve

COPY --from=build /app/dist /app
EXPOSE 80

CMD ["serve", "-s", "/app", "-l", "80"]