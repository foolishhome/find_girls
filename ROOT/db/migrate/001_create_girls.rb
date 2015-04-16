class CreateGirls < ActiveRecord::Migration
  def self.up
    create_table :girls do |t|
      t.string :title
      t.text :body
      t.timestamps
    end
  end

  def self.down
    drop_table :girls
  end
end
