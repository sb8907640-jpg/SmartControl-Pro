const jwt = require('jsonwebtoken');

module.exports = async (socket, next) => {
  try {
    const token = socket.handshake.auth.token;
    
    if (!token) {
      return next(new Error('Authentication required'));
    }

    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    socket.user = {
      id: decoded.id,
      email: decoded.email,
      role: decoded.role,
      deviceId: decoded.deviceId
    };
    
    next();
  } catch (err) {
    next(new Error('Invalid token'));
  }
};