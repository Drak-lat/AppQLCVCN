namespace API.CongViec.Models
{
    public class User
    {
        public int Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public string? FullName { get; set; }
        public string? Email { get; set; }
        public string? Phone { get; set; }
        public DateOnly? Birthdate { get; set; }
        public string? Address { get; set; }
        public string? AvatarUrl { get; set; }
    }
}
