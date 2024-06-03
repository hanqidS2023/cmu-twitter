import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class StompClientSingleton {
  static instance = null;

  static getInstance() {
    if (this.instance === null) {
      this.instance = new StompClientSingleton();
    }
    return this.instance;
  }

  constructor() {
    const socket = new SockJS(`${process.env.REACT_APP_SOCKJS_CLIENT_POST_URL}ws-communitypost`);
    this.client = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        login: 'guest',
        passcode: 'guest'
      },
      reconnectDelay: 500,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    this.setupClient();
    this.client.activate();
  }

  setupClient() {
    this.client.onConnect = function (frame) {
      console.log('Connected: ' + frame);
    };

    this.client.onChangeState = function (frame) {
      console.log('State changed: ' + frame);
    };

    this.client.onStompError = function (frame) {
      console.log('Broker reported error: ' + frame.headers['message']);
      console.log('Additional details: ' + frame.body);
    };
  }

  isClientConnected() {
    return this.client.active;
  }

  async ensureConnection() {
    if (!this.isClientConnected()) {
      try {
        await this.client.activate();
      } catch (error) {
        console.error('Failed to establish STOMP connection:', error);
      }
    }
  }

  disconnect() {
    if (this.client !== null) {
      this.client.deactivate();
    }
    console.log("Disconnected");
  }

  async sendMessage(endpoint, message) {
    await this.ensureConnection();

    if (!this.isClientConnected()) {
      console.error("Failed to send message: STOMP client is not connected.");
      return;
    }

    const messageStr = typeof message === 'object' ? JSON.stringify(message) : message;

    this.client.publish({
      destination: endpoint,
      body: messageStr,
      skipContentLengthHeader: false
    });

    console.log(`Message sent to ${endpoint}: ${messageStr}`);
  }

  async subscribeToTopic(topic, callback) {
    console.log("subscribing to topic: ", topic)
    await this.ensureConnection();

    if (!this.isClientConnected()) {
      console.error("Failed to subscribe: STOMP client is not connected.");
      return null;
    }

    return this.client.subscribe(topic, message => {
      console.log(`Message received from topic ${topic}: ${message.body}`);
      callback(JSON.parse(message.body));
    });
  }
}


export const stompClientInstance = StompClientSingleton.getInstance();
