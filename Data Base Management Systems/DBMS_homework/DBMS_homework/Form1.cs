using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Data.SqlClient;

namespace DBMS_homework
{
    public partial class Form1 : Form
    {

        private SqlConnection connection = new SqlConnection("Data Source=DESKTOP-QSLMK25\\SQLEXPRESS;Initial Catalog=Video_Games;Integrated Security=True");
        private SqlDataAdapter shopTable;
        private DataSet shopSet;

        public Form1()
        {
            InitializeComponent();
            shop_grid.SelectionChanged += new EventHandler(loadChildren);
            loadParent();

        }

        private void loadChildren(object sender, EventArgs e)
        {
            LoadChildren();
        }

        private void LoadChildren()
        {
            try
            {
                int shop_id = Convert.ToInt32(shop_grid.CurrentRow.Cells[0].Value);
                SqlCommand command = new SqlCommand("SELECT * FROM ShopManager WHERE Shop_id=@id")
                {
                    Connection = new SqlConnection("Data Source=DESKTOP-QSLMK25\\SQLEXPRESS;Initial Catalog=Video_Games;Integrated Security=True")
                };
                command.Parameters.AddWithValue("@id", shop_id);

                shopTable = new SqlDataAdapter(command);
                shopSet = new DataSet();

                shopTable.Fill(shopSet, "ShopManager");
                manager_grid.DataSource = shopSet.Tables["ShopManager"];
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void loadParent()
        {
            SqlDataAdapter shopTable = new SqlDataAdapter("SELECT * FROM Shop", "Data Source=DESKTOP-QSLMK25\\SQLEXPRESS;Initial Catalog=Video_Games;Integrated Security=True");
            DataSet dataSet = new DataSet();

            shopTable.Fill(dataSet, "Shop");
            shop_grid.DataSource = dataSet.Tables["Shop"];
        }

        private void button_connect_Click(object sender, EventArgs e)
        {
            SqlConnection conn = new SqlConnection("Data Source=DESKTOP-QSLMK25\\SQLEXPRESS;Initial Catalog=Video_Games;Integrated Security=True");
            SqlDataReader reader = null;
            try
            {
                conn.Open();
                Console.WriteLine("Connection Open!");
                SqlCommand command = new SqlCommand("select count(*) from Shop", conn);
                reader = command.ExecuteReader();
                reader.Read();
                MessageBox.Show(reader[0].ToString());

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                if (reader != null)
                {
                    reader.Close();
                }

                conn.Close();
            }
        }

        private void button_add_Click(object sender, EventArgs e)
        {
            SqlConnection conn = new SqlConnection("Data Source=DESKTOP-QSLMK25\\SQLEXPRESS;Initial Catalog=Video_Games;Integrated Security=True");
            try
            {
                SqlDataAdapter shopTable = new SqlDataAdapter("SELECT * FROM Shop", "Data Source=DESKTOP-QSLMK25\\SQLEXPRESS;Initial Catalog=Video_Games;Integrated Security=True");
                SqlCommand InsertCommand = new SqlCommand("INSERT INTO ShopManager (Name,Shop_id) VALUES (@n, @id)", conn);

                InsertCommand.Parameters.Add("@n", SqlDbType.VarChar).Value = text_name.Text;
                InsertCommand.Parameters.Add("@id", SqlDbType.Int).Value = Convert.ToInt32(text_shopId.Text);


                shopTable = new SqlDataAdapter(InsertCommand);
                shopSet = new DataSet();

                conn.Open();
                InsertCommand.ExecuteNonQuery();
                conn.Close();
                MessageBox.Show("Manager inserted successfully !");

                LoadChildren();

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                conn.Close();
            }
        }

        private void button_update_Click(object sender, EventArgs e)
        {
            SqlConnection conn = new SqlConnection("Data Source=DESKTOP-QSLMK25\\SQLEXPRESS;Initial Catalog=Video_Games;Integrated Security=True");
            try
            {
                SqlDataAdapter shopTable = new SqlDataAdapter("SELECT * FROM Shop", "Data Source=DESKTOP-QSLMK25\\SQLEXPRESS;Initial Catalog=Video_Games;Integrated Security=True");
                SqlCommand UpdateCommand = new SqlCommand("UPDATE ShopManager SET Name=@n, Shop_id=@id WHERE Smid=@sid", conn);

                UpdateCommand.Parameters.Add("@sid", SqlDbType.Int).Value = Convert.ToInt32(text_id.Text);
                UpdateCommand.Parameters.Add("@n", SqlDbType.VarChar).Value = text_name.Text;
                UpdateCommand.Parameters.Add("@id", SqlDbType.Int).Value = Convert.ToInt32(text_shopId.Text);

                shopTable = new SqlDataAdapter(UpdateCommand);

                conn.Open();
                UpdateCommand.ExecuteNonQuery();
                conn.Close();

                LoadChildren();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                conn.Close();
            }
        }

        private void button_delete_Click(object sender, EventArgs e)
        {
            SqlConnection conn = new SqlConnection("Data Source=DESKTOP-QSLMK25\\SQLEXPRESS;Initial Catalog=Video_Games;Integrated Security=True");
            try
            {
                SqlDataAdapter shopTable = new SqlDataAdapter("SELECT * FROM Shop", "Data Source=DESKTOP-QSLMK25\\SQLEXPRESS;Initial Catalog=Video_Games;Integrated Security=True");
                SqlCommand UpdateCommand = new SqlCommand("DELETE FROM ShopManager WHERE Smid=@sid", conn);

                UpdateCommand.Parameters.Add("@sid", SqlDbType.Int).Value = Convert.ToInt32(text_id.Text);
                shopTable = new SqlDataAdapter(UpdateCommand);

                conn.Open();
                UpdateCommand.ExecuteNonQuery();
                conn.Close();

                LoadChildren();
                MessageBox.Show("Manager deleted successfully !");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                conn.Close();
            }
        }
    }
}
