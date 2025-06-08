using API.CongViec.Models;

namespace API.CongViec.Model
{
    public class TaskModel
    {
        public int Id { get; set; }
        public string Title { get; set; } = string.Empty;
        public string Description { get; set; } = string.Empty;
        public DateTime DueDate { get; set; }
        public int Priority { get; set; }
        public bool Completed { get; set; }

        public int UserId { get; set; }
        public User? User { get; set; }
    }
}
