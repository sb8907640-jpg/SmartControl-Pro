const fs = require('fs');
const path = require('path');
const db = require('../config/database');

async function migrate() {
  try {
    console.log('🔄 Running migrations...');
    
    const schema = fs.readFileSync(
      path.join(__dirname, 'schema.sql'),
      'utf8'
    );
    
    await db.query(schema);
    
    console.log('✅ Migrations completed successfully');
    process.exit(0);
  } catch (err) {
    console.error('❌ Migration failed:', err);
    process.exit(1);
  }
}

migrate();