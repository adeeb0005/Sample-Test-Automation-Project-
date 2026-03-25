# Base image with Maven and OpenJDK
FROM maven:3.8.6-openjdk-11 AS builder

# Set working directory
WORKDIR /app

# Install dependencies (cached separately)
RUN apt-get update && apt-get install -y \
    xvfb \
    wget \
    unzip \
    curl \
    gnupg \
    software-properties-common \
    && rm -rf /var/lib/apt/lists/*

# Install Chrome and ChromeDriver (cached separately)
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" | tee /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && CHROME_VERSION=$(google-chrome --version | awk '{print $3}') \
    && wget -q "https://chromedriver.storage.googleapis.com/$(curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE)/chromedriver_linux64.zip" \
    && unzip chromedriver_linux64.zip \
    && mv chromedriver /usr/local/bin/ \
    && chmod +x /usr/local/bin/chromedriver \
    && rm chromedriver_linux64.zip

# Install Firefox and Geckodriver (cached separately)
RUN apt-get install -y firefox-esr \
    && GECKO_VERSION=$(curl -sS https://api.github.com/repos/mozilla/geckodriver/releases/latest | grep "tag_name" | cut -d '"' -f 4) \
    && wget "https://github.com/mozilla/geckodriver/releases/download/$GECKO_VERSION/geckodriver-$GECKO_VERSION-linux64.tar.gz" \
    && tar -xvzf geckodriver-$GECKO_VERSION-linux64.tar.gz \
    && mv geckodriver /usr/local/bin/ \
    && chmod +x /usr/local/bin/geckodriver \
    && rm geckodriver-$GECKO_VERSION-linux64.tar.gz

# Copy only pom.xml first to cache dependencies
COPY pom.xml . 

# Download dependencies to leverage caching
RUN mvn dependency:go-offline

# Copy the entire project (only code changes trigger rebuilds)
COPY . .

# Build the project (without tests for faster build)
RUN mvn clean package -DskipTests

# Define volumes for external properties and reports
VOLUME /config
VOLUME /report

# Command to execute tests dynamically with a specific TestNG XML
CMD ["sh", "-c", "Xvfb :99 -ac & export DISPLAY=:99 && mvn clean test -DsuiteXmlFile=${TEST_SUITE}"]