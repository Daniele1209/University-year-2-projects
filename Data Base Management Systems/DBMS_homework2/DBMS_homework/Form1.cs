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
using System.Configuration;

namespace DBMS_homework
{
    public partial class Form1 : Form
    {
        SqlDataAdapter da = new SqlDataAdapter();
        DataSet ds = new DataSet();
        public Form1()
        {
                InitializeComponent();
                parent_grid.SelectionChanged += new EventHandler(loadChildren);
                LoadParent();

        }

        private void loadChildren(object sender, EventArgs e)
        {
            LoadChildren();
        }

        private void LoadChildren()
        {
            string con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
            SqlDataAdapter da = new SqlDataAdapter();
            DataSet ds = new DataSet();

            try
            {
                SqlConnection cs = new SqlConnection(con);
                string TableName = ConfigurationManager.AppSettings["TableNameChildren"];
                string ParentId = ConfigurationManager.AppSettings["ParentId"];

                int parent_id = Convert.ToInt32(parent_grid.CurrentRow.Cells[0].Value);
                SqlCommand command = new SqlCommand("SELECT * FROM " + TableName + " WHERE " + ParentId + "=@id", cs);

                command.Parameters.AddWithValue("@id", parent_id);

                da.SelectCommand = command;
                ds.Clear();
                da.Fill(ds, TableName);
                child_grid.DataSource = ds.Tables[TableName];
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void LoadParent()
        {
            string con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
            SqlDataAdapter da = new SqlDataAdapter();
            DataSet ds = new DataSet();

            try
            {
                string select = ConfigurationSettings.AppSettings["selectParent"];
                SqlConnection cs = new SqlConnection(con);
                string TableName = ConfigurationManager.AppSettings["TableNameParent"];

                da.SelectCommand = new SqlCommand(select, cs);
                ds.Clear();
                da.Fill(ds, TableName);
                parent_grid.DataSource = ds.Tables[TableName];
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
        private void button_add_Click(object sender, EventArgs e)
        {
            string con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
            SqlConnection cs = new SqlConnection(con);
            try
            {
                string TableName = ConfigurationManager.AppSettings["TableNameChildren"];
                string ColumnNames = ConfigurationManager.AppSettings["ColumnNamesChildren"];
                string ColumnParameters = ConfigurationManager.AppSettings["ColumnNamesParameters"];
                List<string> columnNamesList = new List<string>(ConfigurationManager.AppSettings["ColumnNamesChildren"].Split(','));
                SqlCommand command = new SqlCommand("INSERT INTO " + TableName + " (" + ColumnNames + ") VALUES (" + ColumnParameters + ")", cs);

                foreach(string column in columnNamesList)
                {
                    TextBox box = (TextBox)panel1.Controls[column];
                    command.Parameters.AddWithValue("@" + column, box.Text);
                }
                cs.Open();
                SqlDataAdapter da = new SqlDataAdapter(command);
                ds.Clear();
                da.Fill(ds, TableName);
                MessageBox.Show("Added successfuly !");
                cs.Close();

                LoadChildren();

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                cs.Close();
            }
        }

        private void button_update_Click(object sender, EventArgs e)
        {
            string con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
            SqlConnection cs = new SqlConnection(con);
            try
            {
                string TableName = ConfigurationManager.AppSettings["TableNameChildren"];
                string ChildId = ConfigurationManager.AppSettings["ChildId"];
                string ColumnNames = ConfigurationManager.AppSettings["ColumnNamesChildren"];
                string ColumnParameters = ConfigurationManager.AppSettings["ColumnNamesParameters"];
                List<string> columnNamesList = new List<string>(ConfigurationManager.AppSettings["ColumnNamesChildren"].Split(','));
                string update = ConfigurationManager.AppSettings["UpdateQuerry"];
                SqlCommand command = new SqlCommand(update, cs);

                string co = child_grid.CurrentRow.Cells[ChildId].Value.ToString();
                int id = Int32.Parse(child_grid.CurrentRow.Cells[ChildId].Value.ToString());
                command.Parameters.AddWithValue("@id", id);

                foreach (string column in columnNamesList)
                {
                    TextBox box = (TextBox)panel1.Controls[column];
                    command.Parameters.AddWithValue("@" + column, box.Text);
                }
                cs.Open();
                SqlDataAdapter da = new SqlDataAdapter(command);
                ds.Clear();
                da.Fill(ds, TableName);
                MessageBox.Show("Updated successfuly !");
                cs.Close();

                LoadChildren();

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                cs.Close();
            }
        }

        private void button_delete_Click(object sender, EventArgs e)
        {
            string con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
            SqlConnection cs = new SqlConnection(con);
            try
            {
                string TableName = ConfigurationManager.AppSettings["TableNameChildren"];
                string ChildId = ConfigurationManager.AppSettings["ChildId"];
                SqlCommand command = new SqlCommand("DELETE FROM " + TableName + " WHERE " + ChildId + " = @id", cs);

                cs.Open();
                command.Parameters.AddWithValue("@id", Convert.ToInt32(child_grid.CurrentRow.Cells[ChildId].Value.ToString()));
                SqlDataAdapter da = new SqlDataAdapter(command);
                ds.Clear();
                da.Fill(ds, TableName);
                MessageBox.Show("Deleted successfuly !");
                cs.Close();

                LoadChildren();

            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                cs.Close();
            }
        }

        private void button_generate_Click(object sender, EventArgs e)
        {
            try
            {
                List<string> columnNames = new List<string>(ConfigurationManager.AppSettings["ColumnNamesChildren"].Split(','));
                int pointX = 30;
                int pointY = 40;
                int column_numbers = Convert.ToInt32(ConfigurationManager.AppSettings["NumberOfColumnsChildren"]);
                panel1.Controls.Clear();
                foreach (string column in columnNames)
                {
                    TextBox box = new TextBox();
                    box.Text = column;
                    box.Name = column;
                    box.Location = new Point(pointX, pointY);
                    box.Visible = true;
                    box.Parent = panel1;
                    panel1.Show();
                    pointY += 30;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
         }

        private void child_grid_RowHeaderMouseClick(object sender, DataGridViewCellMouseEventArgs e)
        {
            List<string> ColumnNames = new List<string>(ConfigurationManager.AppSettings["ColumnNamesChildren"].Split(','));
            int cid = Convert.ToInt32(child_grid.CurrentRow.Cells[0].Value.ToString());
            int i = 1;
            foreach (string column in ColumnNames)
            {
                TextBox textBox = (TextBox)panel1.Controls[column];
                textBox.Text = child_grid.CurrentRow.Cells[i].Value.ToString();
                i++;
            }
        }
    }
}
